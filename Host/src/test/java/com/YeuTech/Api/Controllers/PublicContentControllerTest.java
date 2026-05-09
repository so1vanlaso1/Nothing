package com.YeuTech.Api.Controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.YeuTech.Domain.Entities.CmsContent;
import com.YeuTech.Domain.Entities.CmsContentPublication;
import com.YeuTech.Domain.Entities.CustomDomain;
import com.YeuTech.Domain.Repository.ICmsContentPublicationRepository;
import com.YeuTech.Domain.Repository.ICmsRepository;
import com.YeuTech.Domain.Repository.ICustomDomainRepository;

import static org.junit.jupiter.api.Assertions.*;

class PublicContentControllerTest {

    private static final String USER_ID = "user-001";
    private static final String DOMAIN_ID = "dom-001";
    private static final String CONTENT_ID = "cnt-001";

    private StubDomainRepo domainRepo;
    private StubPubRepo pubRepo;
    private StubCmsRepo cmsRepo;
    private PublicContentController controller;

    @BeforeEach
    void setUp() {
        domainRepo = new StubDomainRepo();
        pubRepo = new StubPubRepo();
        cmsRepo = new StubCmsRepo();
        controller = new PublicContentController(domainRepo, pubRepo, cmsRepo);
    }

    @Test @DisplayName("200 OK with HTML for valid request")
    void resolveSuccess() {
        setupValidScenario();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "example.com");

        ResponseEntity<String> resp = controller.resolvePublicContent("test-slug", CONTENT_ID, req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        String body = resp.getBody();
        assertNotNull(body);
        assertTrue(body.contains("og:title"));
        assertTrue(body.contains("og:url"));
        assertTrue(body.contains("canonical"));
        assertTrue(body.contains("Test Title"));
    }

    @Test @DisplayName("301 redirect on slug mismatch")
    void slugMismatchRedirect() {
        setupValidScenario();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "example.com");

        ResponseEntity<String> resp = controller.resolvePublicContent("wrong-slug", CONTENT_ID, req);

        assertEquals(HttpStatus.MOVED_PERMANENTLY, resp.getStatusCode());
        assertEquals("https://example.com/p/test-slug/" + CONTENT_ID,
                resp.getHeaders().getFirst("Location"));
    }

    @Test @DisplayName("404 for unknown domain")
    void unknownDomain() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "unknown.com");

        ResponseEntity<String> resp = controller.resolvePublicContent("slug", "id", req);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test @DisplayName("404 for inactive domain")
    void inactiveDomain() {
        CustomDomain d = makeDomain();
        d.setActive(false);
        domainRepo.findResult = Optional.of(d);
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "example.com");

        ResponseEntity<String> resp = controller.resolvePublicContent("slug", CONTENT_ID, req);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test @DisplayName("404 for no publication")
    void noPublication() {
        domainRepo.findResult = Optional.of(makeDomain());
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "example.com");

        ResponseEntity<String> resp = controller.resolvePublicContent("slug", CONTENT_ID, req);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test @DisplayName("404 for ownership mismatch")
    void ownershipMismatch() {
        CustomDomain d = makeDomain();
        d.setUserId("different-user");
        domainRepo.findResult = Optional.of(d);
        pubRepo.findResult = Optional.of(makePub());
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "example.com");

        ResponseEntity<String> resp = controller.resolvePublicContent("test-slug", CONTENT_ID, req);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test @DisplayName("404 for unpublished content")
    void unpublishedContent() {
        domainRepo.findResult = Optional.of(makeDomain());
        pubRepo.findResult = Optional.of(makePub());
        // cmsRepo returns empty (content not PUBLISHED)
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "example.com");

        ResponseEntity<String> resp = controller.resolvePublicContent("test-slug", CONTENT_ID, req);
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test @DisplayName("Resolves X-Forwarded-Host header")
    void forwardedHost() {
        setupValidScenario();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("X-Forwarded-Host", "example.com");
        req.addHeader("Host", "internal-host:8080");

        ResponseEntity<String> resp = controller.resolvePublicContent("test-slug", CONTENT_ID, req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    @Test @DisplayName("Strips port from Host header")
    void stripPort() {
        setupValidScenario();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Host", "example.com:443");

        ResponseEntity<String> resp = controller.resolvePublicContent("test-slug", CONTENT_ID, req);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

    // ---- Setup helpers ----

    private void setupValidScenario() {
        domainRepo.findResult = Optional.of(makeDomain());
        pubRepo.findResult = Optional.of(makePub());
        cmsRepo.findResult = Optional.of(makeContent());
    }

    private CustomDomain makeDomain() {
        CustomDomain d = new CustomDomain();
        d.setDomainId(DOMAIN_ID); d.setUserId(USER_ID);
        d.setDomainName("example.com"); d.setActive(true);
        d.setVerificationStatus("VERIFIED"); d.setDnsStatus("POINTED");
        return d;
    }

    private CmsContentPublication makePub() {
        CmsContentPublication p = new CmsContentPublication();
        p.setPublicationId("pub-001"); p.setContentId(CONTENT_ID);
        p.setUserId(USER_ID); p.setDomainId(DOMAIN_ID);
        p.setSlug("test-slug");
        p.setCanonicalUrl("https://example.com/p/test-slug/" + CONTENT_ID);
        p.setActive(true); p.setPublishedDate(LocalDateTime.now());
        return p;
    }

    private CmsContent makeContent() {
        CmsContent c = new CmsContent();
        c.setContentId(CONTENT_ID); c.setUserId(USER_ID);
        c.setTitle("Test Title"); c.setContentBody("<p>Hello</p>");
        c.setStatus("PUBLISHED");
        return c;
    }

    // ---- Stubs ----

    static class StubDomainRepo implements ICustomDomainRepository {
        Optional<CustomDomain> findResult = Optional.empty();
        public CustomDomain save(CustomDomain d) { return d; }
        public Optional<CustomDomain> findByDomainId(String id) { return findResult; }
        public Optional<CustomDomain> findByDomainName(String n) { return findResult; }
        public Optional<CustomDomain> findByDomainIdAndUserId(String id, String u) { return findResult; }
        public List<CustomDomain> findByUserId(String u) { return List.of(); }
        public boolean existsByDomainName(String n) { return false; }
    }

    static class StubPubRepo implements ICmsContentPublicationRepository {
        Optional<CmsContentPublication> findResult = Optional.empty();
        public CmsContentPublication save(CmsContentPublication p) { return p; }
        public Optional<CmsContentPublication> findByDomainIdAndContentId(String d, String c) { return findResult; }
        public Optional<CmsContentPublication> findByDomainIdAndContentIdAndIsActive(String d, String c, boolean a) { return findResult; }
        public Optional<CmsContentPublication> findByContentId(String c) { return findResult; }
        public List<CmsContentPublication> findByUserId(String u) { return List.of(); }
    }

    static class StubCmsRepo implements ICmsRepository {
        Optional<CmsContent> findResult = Optional.empty();
        public List<CmsContent> findAll() { return List.of(); }
        public CmsContent save(CmsContent c) { return c; }
        public Optional<CmsContent> findByContentIdAndUserId(String c, String u) { return Optional.empty(); }
        public List<CmsContent> findRecentByUserId(String u, int l) { return List.of(); }
        public org.springframework.data.domain.Page<CmsContent> findByUserIdWithFilters(
                String u, String s, String ct, String p, Boolean hg,
                java.time.LocalDateTime f, java.time.LocalDateTime t, String se,
                org.springframework.data.domain.Pageable pg) { return org.springframework.data.domain.Page.empty(); }
        public void delete(CmsContent c) {}
        public boolean existsByContentIdAndUserId(String c, String u) { return false; }
        public Optional<CmsContent> findByContentIdAndUserIdAndStatus(String c, String u, String s) { return findResult; }
        public long countByUserIdAndCreateDateBetween(String u, java.time.LocalDateTime s, java.time.LocalDateTime e) { return 0; }
        public long countByUserIdAndStatusAndCreateDateBetween(String u, String st, java.time.LocalDateTime s, java.time.LocalDateTime e) { return 0; }
    }
}
