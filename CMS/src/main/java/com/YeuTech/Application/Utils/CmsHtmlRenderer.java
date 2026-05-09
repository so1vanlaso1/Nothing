package com.YeuTech.Application.Utils;

import com.YeuTech.Domain.Entities.CmsContent;

public final class CmsHtmlRenderer {

    // 1. Define the HTML structure as a clear, readable Text Block
    private static final String HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%1$s</title>
                <style>
                    * { 
                        margin: 0; 
                        padding: 0; 
                        box-sizing: border-box; 
                    }
                    body { 
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; 
                        line-height: 1.7; 
                        color: #1a1a2e;
                        background: #fafbfc; 
                    }
                    .header { 
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        color: white; 
                        padding: 2rem 1rem; 
                        text-align: center; 
                    }
                    .header h1 { 
                        font-size: 2rem; 
                        font-weight: 700; 
                        max-width: 800px;
                        margin: 0 auto; 
                    }
                    article { 
                        max-width: 800px; 
                        margin: 2rem auto; 
                        padding: 2rem;
                        background: white; 
                        border-radius: 8px;
                        box-shadow: 0 1px 3px rgba(0,0,0,0.1); 
                    }
                    article img { 
                        max-width: 100%%; 
                        height: auto; 
                        border-radius: 4px; 
                    }
                    article h2, article h3 { 
                        margin: 1.5rem 0 0.75rem; 
                    }
                    article p { 
                        margin-bottom: 1rem; 
                    }
                    article ul, article ol { 
                        margin: 0.5rem 0 1rem 1.5rem; 
                    }
                    .footer { 
                        text-align: center; 
                        padding: 2rem; 
                        font-size: 0.8rem;
                        color: #999; 
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>%1$s</h1>
                </div>
                <article>
                    %2$s
                </article>
                <div class="footer">
                    <p>Powered by YeuTech</p>
                </div>
            </body>
            </html>
            """;

    private CmsHtmlRenderer() {
        // Prevent instantiation of utility class
    }

    public static String renderHtml(CmsContent content) {
        if (content == null) {
            throw new IllegalArgumentException("content is required");
        }

        // 2. Extract and sanitize variables
        String rawTitle = content.getTitle() != null ? content.getTitle() : "Untitled";
        String escapedTitle = escapeHtml(rawTitle);
        String contentBody = content.getContentBody() != null ? content.getContentBody() : "";

        // 3. Inject variables into the template
        return String.format(HTML_TEMPLATE, escapedTitle, contentBody);
    }

    private static String escapeHtml(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}