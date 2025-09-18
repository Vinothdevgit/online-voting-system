package com.bishop.heber.voting.service.impl;

import com.bishop.heber.voting.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.safety.Safelist;
import org.jsoup.safety.Cleaner;

import java.net.URI;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BhcNewsService {
    private static final String SOURCE = "https://bhc.edu.in/bhc/index.php";
    private static final Duration TTL = Duration.ofMinutes(10);

    private volatile Instant lastFetch = Instant.EPOCH;
    private volatile BhcNewsEvents cache;
    private volatile String lastError = null;

    public BhcNewsEvents fetch() {
        // simple 10-min cache
        if (cache != null && Instant.now().isBefore(lastFetch.plus(TTL))) return cache;

        try {
            lastError = null;
            Document doc = Jsoup.connect(SOURCE)
                    .userAgent("Mozilla/5.0 (BHC-Dashboard/1.0)")
                    .referrer("https://www.google.com/")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .timeout(15000)
                    .get();

            // quick sanity: log title & length
            String title = doc.title();
            int textLen = doc.text().length();
            log.info("doc >>>>>> {}",doc);
            System.out.println("[BHC] Fetched OK: title=" + title + ", textLen=" + textLen);

            List<BhcItem> news   = extractSection(doc, "News");
            List<BhcItem> events = extractSection(doc, "Events");

            if (news.isEmpty() && events.isEmpty()) {
                System.out.println("[BHC] Parsed empty lists — selectors may need adjustment");
            }

            cache = new BhcNewsEvents(SOURCE, Instant.now(), news, events);
            lastFetch = Instant.now();
            return cache;

        } catch (Exception e) {
            lastError = e.getClass().getSimpleName() + ": " + e.getMessage();
            System.out.println("[BHC] Fetch failed: " + lastError);
            // Return last good cache if any, else empty
            return cache != null ? cache : new BhcNewsEvents(SOURCE, Instant.now(), List.of(), List.of());
        }
    }

    /** Extract items under a heading like "News" or "Events", with multiple fallbacks. */
    private List<BhcItem> extractSection(Document doc, String headingText) {
        // Strategy A: Header → next blocks → parse
        Element header = selectHeader(doc, headingText);
        if (header != null) {
            List<BhcItem> fromHeaderBlock = parseFromBlocks(collectBlocksAround(header));
            if (!fromHeaderBlock.isEmpty()) {
                System.out.println("[BHC] " + headingText + ": header strategy found " + fromHeaderBlock.size());
                return limitAndDedup(fromHeaderBlock);
            }
        }

        // Strategy B: containers with id/class containing "news"/"events"
        String needle = headingText.toLowerCase(Locale.ROOT);
        Elements containers = doc.select("[id*=" + needle + " i], [class*=" + needle + " i]");
        if (!containers.isEmpty()) {
            List<BhcItem> fromContainers = parseFromBlocks(containers);
            if (!fromContainers.isEmpty()) {
                System.out.println("[BHC] " + headingText + ": class/id strategy found " + fromContainers.size());
                return limitAndDedup(fromContainers);
            }
        }

        // Strategy C: look for any element whose own text contains the word, then parse its parent area
        Element anyLabel = doc.selectFirst(":matchesOwn((?i)\\b" + Pattern.quote(headingText) + "\\b)");
        if (anyLabel != null) {
            List<BhcItem> around = parseFromBlocks(collectBlocksAround(anyLabel));
            if (!around.isEmpty()) {
                System.out.println("[BHC] " + headingText + ": loose-label strategy found " + around.size());
                return limitAndDedup(around);
            }
        }

        System.out.println("[BHC] " + headingText + ": no items found");
        return List.of();
    }

    /** Prefer li>a; fall back to any a[href] inside the given blocks. */
    private List<BhcItem> parseFromBlocks(Collection<Element> blocks) {
        List<BhcItem> out = new ArrayList<>();

        // 1) All <li> in blocks (deep)
        Elements lis = new Elements();
        blocks.forEach(b -> lis.addAll(b.select("li")));
        if (!lis.isEmpty()) {
            lis.forEach(li -> {
                BhcItem it = fromLi(li);
                if (it != null) out.add(it);
            });
        }

        // 2) If still empty, any <a href> in blocks
        if (out.isEmpty()) {
            Elements anchors = new Elements();
            blocks.forEach(b -> anchors.addAll(b.select("a[href]")));
            anchors.forEach(a -> {
                BhcItem it = fromAnchor(a);
                if (it != null) out.add(it);
            });
        }

        return out;
    }

    /** Collect a generous set of nodes around a label/header, including siblings and parent section. */
    private Collection<Element> collectBlocksAround(Element label) {
        List<Element> blocks = new ArrayList<>();

        // a) siblings until next header at the same level
        blocks.addAll(collectUntilNextHeader(label));

        // b) parent’s following siblings until next header
        Element parent = label.parent();
        if (parent != null) {
            blocks.addAll(collectUntilNextHeader(parent));
        }

        // c) include the parent itself (to catch nested lists)
        if (parent != null) blocks.add(parent);

        // de-dup while preserving order
        LinkedHashSet<Element> uniq = new LinkedHashSet<>(blocks);
        return uniq;
    }

    /** Find a proper <h1>-<h6> whose own text matches "News"/"Events" (case-insensitive). */
    private Element selectHeader(Document doc, String text) {
        String rxExact = "(?i)^\\s*" + Pattern.quote(text) + "\\s*$";
        for (String h : List.of("h1","h2","h3","h4","h5","h6")) {
            Element exact = doc.selectFirst(h + ":matchesOwn(" + rxExact + ")");
            if (exact != null) return exact;
        }
        String rxLoose = "(?i)\\b" + Pattern.quote(text) + "\\b";
        for (String h : List.of("h1","h2","h3","h4","h5","h6")) {
            Element loose = doc.selectFirst(h + ":matchesOwn(" + rxLoose + ")");
            if (loose != null) return loose;
        }
        return null;
    }
    private List<Element> collectUntilNextHeader(Element start) {
        List<Element> out = new ArrayList<>();
        for (Element el = start.nextElementSibling(); el != null; el = el.nextElementSibling()) {
            if (isHeader(el)) break;
            out.add(el);
        }
        return out;
    }

    private boolean isHeader(Element e) {
        String t = e.tagName().toLowerCase(Locale.ROOT);
        return t.length() == 2 && t.charAt(0) == 'h' && Character.isDigit(t.charAt(1));
    }

    /** Limit & de-duplicate by title. */
    private List<BhcItem> limitAndDedup(List<BhcItem> items) {
        return items.stream()
                .collect(Collectors.toMap(BhcItem::title, i -> i, (a,b)->a, LinkedHashMap::new))
                .values()
                .stream().limit(12)
                .toList();
    }


    private BhcItem fromLi(Element li) {
        Element clone = li.clone();
        String html = sanitizeHtml(clone);
        // Title = text without HTML; URL = first link if present
        Element firstA = clone.selectFirst("a[href]");
        String url = firstA != null ? firstA.attr("href") : SOURCE;
        String title = li.text().trim();
        return title.isBlank() && (firstA != null ? firstA.text().isBlank() : true)
                ? null
                : new BhcItem(title.isBlank() && firstA != null ? firstA.text().trim() : title, url, html);
    }

    private BhcItem fromAnchor(Element a) {
        // Prefer the full container that holds both the plain text and the <a>
        Element container = a.closest("li, p, div");
        if (container != null) {
            Element clone = container.clone();
            String html = sanitizeHtml(clone);  // keeps both text and links
            String url  = a.absUrl("href");
            if (url == null || url.isBlank()) url = a.attr("href");
            String title = container.text().trim();
            if (title.isBlank() && a.text() != null) title = a.text().trim();
            return new BhcItem(title, url, html);
        }

        // Fallback (rare): just the anchor
        Element clone = a.clone();
        String html = sanitizeHtml(clone);
        String url  = clone.absUrl("href");
        if (url == null || url.isBlank()) url = clone.attr("href");
        String title = a.text().trim();
        if (title.isBlank() && (url == null || url.isBlank())) return null;
        return new BhcItem(title.isBlank() ? url : title, url, html);
    }
    private String sanitizeHtml(Element root) {
        // Work on a clone so we don’t mutate the original
        Element clone = root.clone();

        // Absolutize links + set target/rel
        for (Element a : clone.select("a[href]")) {
            String href = a.attr("href");
            if (href != null && !href.isBlank()) {
                if (!href.startsWith("http")) {
                    href = URI.create(SOURCE).resolve(href).toString();
                }
                a.attr("href", href);
            }
            a.attr("target", "_blank");
            a.attr("rel", "noreferrer");
        }

        // Allow simple inline markup + anchors
        Safelist safe = Safelist.relaxed()
                .addTags("span", "br")
                .addAttributes("a", "href", "target", "rel")
                .removeAttributes("a", "class", "style");

        // Clean the element’s HTML and return it
        return Jsoup.clean(clone.html(), SOURCE, safe);
    }


    // Optional debug accessor if you want a /public/bhc/debug endpoint later:
    public String getLastError() { return lastError; }
}
