package com.YeuTech.Api.Controllers;

import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Domain.Entities.CmsContent;
import com.YeuTech.Domain.Entities.CmsContentPublication;
import com.YeuTech.Domain.Entities.CustomDomain;
import com.YeuTech.Domain.Repository.ICmsContentPublicationRepository;
import com.YeuTech.Domain.Repository.ICmsRepository;
import com.YeuTech.Domain.Repository.ICustomDomainRepository;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Public unauthenticated controller for serving published CMS content
 * on custom domains. Resolves: Host header -> domain -> publication -> content.
 */
@RestController
public class PublicContentController {

    private static final String STATUS_PUBLISHED = "PUBLISHED";

    private final ICustomDomainRepository domainRepository;
    private final ICmsContentPublicationRepository publicationRepository;
    private final ICmsRepository cmsRepository;

    public PublicContentController(
            ICustomDomainRepository domainRepository,
            ICmsContentPublicationRepository publicationRepository,
            ICmsRepository cmsRepository) {
        this.domainRepository = domainRepository;
        this.publicationRepository = publicationRepository;
        this.cmsRepository = cmsRepository;
    }

    @GetMapping("/p/{slug}/{contentId}")
    public ResponseEntity<String> resolvePublicContent(
            @PathVariable("slug") String slug,
            @PathVariable("contentId") String contentId,
            HttpServletRequest request) {

        // 1. Resolve host from request headers
        String host = resolveHost(request);
        if (host == null) {
            return notFoundHtml("Invalid request: no host header");
        }

        // 2. Find active verified domain by host
        Optional<CustomDomain> domainOpt = domainRepository.findByDomainName(host);
        if (domainOpt.isEmpty()) {
            return notFoundHtml("Domain not found");
        }
        CustomDomain domain = domainOpt.get();
        if (!domain.isActive()) {
            return notFoundHtml("Domain is not active");
        }

        // 3. Find active publication by domain + contentId
        Optional<CmsContentPublication> pubOpt = publicationRepository
                .findByDomainIdAndContentIdAndIsActive(domain.getDomainId(), contentId, true);
        if (pubOpt.isEmpty()) {
            return notFoundHtml("Content not found");
        }
        CmsContentPublication publication = pubOpt.get();

        // 4. Strict tenant isolation: publication owner must match domain owner
        if (!publication.getUserId().equals(domain.getUserId())) {
            return notFoundHtml("Content not found");
        }

        // 5. Load CMS content and require PUBLISHED status
        Optional<CmsContent> contentOpt = cmsRepository.findByContentIdAndUserIdAndStatus(
                contentId, publication.getUserId(), STATUS_PUBLISHED);
        if (contentOpt.isEmpty()) {
            return notFoundHtml("Content not found or not published");
        }
        CmsContent content = contentOpt.get();

        // 6. Slug mismatch -> 301 redirect to canonical URL
        if (!slug.equals(publication.getSlug())) {
            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .header(HttpHeaders.LOCATION, publication.getCanonicalUrl())
                    .build();
        }

        // 7. Render HTML with OG tags
        String html = renderHtml(content, publication, domain);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    /**
     * Resolve hostname from X-Forwarded-Host or Host header, normalized to lowercase without port.
     */
    private String resolveHost(HttpServletRequest request) {
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null || host.isBlank()) {
            host = request.getHeader("Host");
        }
        if (host == null || host.isBlank()) {
            return null;
        }
        // Strip port if present
        int colonIndex = host.indexOf(':');
        if (colonIndex > 0) {
            host = host.substring(0, colonIndex);
        }
        return host.trim().toLowerCase(Locale.ROOT);
    }

    private ResponseEntity<String> notFoundHtml(String message) {
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Not Found</title>
                    <style>
                        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                               display: flex; justify-content: center; align-items: center; min-height: 100vh;
                               margin: 0; background: #f8f9fa; color: #495057; }
                        .container { text-align: center; padding: 2rem; }
                        h1 { font-size: 3rem; color: #dee2e6; margin-bottom: 0.5rem; }
                        p { font-size: 1.1rem; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>404</h1>
                        <p>%s</p>
                    </div>
                </body>
                </html>
                """.formatted(escapeHtml(message));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    private String renderHtml(CmsContent content, CmsContentPublication publication, CustomDomain domain) {
        String title = escapeHtml(content.getTitle() != null ? content.getTitle() : "Untitled");
        String description = escapeHtml(publication.getSummary() != null ? publication.getSummary() : "");
        String canonicalUrl = escapeHtml(publication.getCanonicalUrl());
        String coverImage = publication.getCoverImageUrl() != null
                ? escapeHtml(publication.getCoverImageUrl())
                : "";
        String contentBody = content.getContentBody() != null ? content.getContentBody() : "";

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"UTF-8\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("    <title>").append(title).append("</title>\n");
        sb.append("    <meta name=\"description\" content=\"").append(description).append("\">\n");
        sb.append("    <link rel=\"canonical\" href=\"").append(canonicalUrl).append("\">\n");

        // Open Graph tags
        sb.append("    <meta property=\"og:type\" content=\"article\">\n");
        sb.append("    <meta property=\"og:title\" content=\"").append(title).append("\">\n");
        sb.append("    <meta property=\"og:description\" content=\"").append(description).append("\">\n");
        sb.append("    <meta property=\"og:url\" content=\"").append(canonicalUrl).append("\">\n");
        if (!coverImage.isEmpty()) {
            sb.append("    <meta property=\"og:image\" content=\"").append(coverImage).append("\">\n");
        }

        // Twitter Card tags
        sb.append("    <meta name=\"twitter:card\" content=\"summary_large_image\">\n");
        sb.append("    <meta name=\"twitter:title\" content=\"").append(title).append("\">\n");
        sb.append("    <meta name=\"twitter:description\" content=\"").append(description).append("\">\n");
        if (!coverImage.isEmpty()) {
            sb.append("    <meta name=\"twitter:image\" content=\"").append(coverImage).append("\">\n");
        }

        // Styles
        sb.append("    <style>\n");
        sb.append("        * { margin: 0; padding: 0; box-sizing: border-box; }\n");
        sb.append("        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,\n");
        sb.append("               'Helvetica Neue', Arial, sans-serif; line-height: 1.7; color: #1a1a2e;\n");
        sb.append("               background: #fafbfc; }\n");
        sb.append("        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        sb.append("                  color: white; padding: 2rem 1rem; text-align: center; }\n");
        sb.append("        .header h1 { font-size: 2rem; font-weight: 700; max-width: 800px;\n");
        sb.append("                     margin: 0 auto; }\n");
        sb.append("        .header .domain { font-size: 0.85rem; opacity: 0.8; margin-top: 0.5rem; }\n");
        sb.append("        article { max-width: 800px; margin: 2rem auto; padding: 2rem;\n");
        sb.append("                  background: white; border-radius: 8px;\n");
        sb.append("                  box-shadow: 0 1px 3px rgba(0,0,0,0.1); }\n");
        sb.append("        article img { max-width: 100%; height: auto; border-radius: 4px; }\n");
        sb.append("        article h2, article h3 { margin: 1.5rem 0 0.75rem; }\n");
        sb.append("        article p { margin-bottom: 1rem; }\n");
        sb.append("        article ul, article ol { margin: 0.5rem 0 1rem 1.5rem; }\n");
        sb.append("        .footer { text-align: center; padding: 2rem; font-size: 0.8rem;\n");
        sb.append("                  color: #999; }\n");
        sb.append("    </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("    <div class=\"header\">\n");
        sb.append("        <h1>").append(title).append("</h1>\n");
        sb.append("        <div class=\"domain\">").append(escapeHtml(domain.getDomainName())).append("</div>\n");
        sb.append("    </div>\n");

        if (!coverImage.isEmpty()) {
            sb.append("    <div style=\"text-align:center; padding: 1rem;\">\n");
            sb.append("        <img src=\"").append(coverImage).append("\" alt=\"")
              .append(title).append("\" style=\"max-width:800px; width:100%; border-radius:8px;\">\n");
            sb.append("    </div>\n");
        }

        sb.append("    <article>\n");
        sb.append("        ").append(contentBody).append("\n");
        sb.append("    </article>\n");
        sb.append("    <div class=\"footer\">\n");
        sb.append("        <p>Powered by YeuTech</p>\n");
        sb.append("    </div>\n");
        sb.append("</body>\n");
        sb.append("</html>");
        return sb.toString();
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
