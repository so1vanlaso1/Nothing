# #YeuTech Marketing Backend (DigiSite)

## #Cấu trúc dự án

Dự án tuân theo cấu trúc đa module, được tổ chức theo kiến trúc rành mạch:

```text
├── Account/                   # User account management module
│   ├── src/main/java/com/YeuTech/
│   │   ├── Api/               # API Controllers
│   │   ├── Application/       # Application Services
│   │   ├── Domain/            # Core Entities & Domain Interfaces
│   │   ├── Dtos/              # Data Transfer Objects
│   │   └── Infrastructure/    # JPA Repositories & Database Models
│   └── pom.xml                # Maven configuration for Account module
├── Host/                      # Main application startup module
│   ├── src/main/java/com/YeuTech/
│   │   └── Main.java          # Spring Boot entry point class
│   ├── src/main/resources/
│   │   └── application.properties # Environment configuration file
│   └── pom.xml                # Maven configuration for Host module
├── pom.xml                    # Root Maven configuration (dependency management)
└── README.md                  # Project documentation
```

## #Công nghệ sử dụng

- **Java 21.0.1**
- **Spring Boot v3.2.5**
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
- **Apache Maven 3.9.5**

## #Tính năng mới

Dự án hiện hỗ trợ đầy đủ các luồng sau:

- Đăng ký tài khoản bằng email và mật khẩu, có mã OTP xác minh email.
- Đăng nhập bằng JWT access token và refresh token.
- Gia hạn token bằng refresh token với cơ chế xoay vòng token.
- Kích hoạt lại tài khoản bằng cách gửi lại mã xác thực email.
- Quên mật khẩu qua OTP email, nhận verification token và đặt lại mật khẩu mới.
- Quản lý người dùng cho admin: xem danh sách user và thêm user mới.
- Tài liệu API bằng Swagger/OpenAPI.
- Bảo mật stateless bằng Spring Security, JWT filter và CORS cấu hình sẵn.
- Migrations database bằng Flyway.
- Kết nối và quản lý Facebook Page qua access token.
- Tự động hoán đổi short-lived token sang long-lived token (60 ngày) và page access token
- Đăng bài tự động lên Facebook Page đã kết nối.
- Đăng ký và quản lý tên miền tuỳ chỉnh (Custom Domain) cho CMS.
- Xuất bản bài viết CMS lên tên miền tuỳ chỉnh với SEO metadata.
- API Route Public phục vụ phân giải tên miền tuỳ chỉnh và hiển thị nội dung cho người dùng cuối.

## #API chính

### Authentication

- `GET /v1/api/auth/status` - Kiểm tra trạng thái dịch vụ.
- `POST /v1/api/auth/register` - Đăng ký tài khoản mới.
- `POST /v1/api/auth/login` - Đăng nhập và lấy JWT.
- `POST /v1/api/auth/refreshToken` - Làm mới access token bằng refresh token.
- `POST /v1/api/auth/active` - Kích hoạt tài khoản bằng OTP.
- `POST /v1/api/auth/reactive` - Gửi lại OTP kích hoạt.

### Password reset

- `POST /v1/api/forgot/request` - Gửi OTP quên mật khẩu qua email.
- `POST /v1/api/forgot/verify` - Xác thực OTP và nhận verification token.
- `POST /v1/api/forgot/reset` - Đặt lại mật khẩu bằng verification token.

### Admin users

- `GET /v1/api/admin/users` - Lấy danh sách user.
- `POST /v1/api/admin/addUser` - Thêm user mới.

### Facebook Marketing Config

- `GET /v1/api/marketing/config/facebook/token/status` - Kiểm tra trạng thái kết nối Facebook (token còn hạn hay hết hạn, có thể tự động làm mới không).
- `PUT /v1/api/marketing/config/facebook/token` - Lưu/cập nhật access token và page ID (tự động hoán đổi sang long-lived token nếu trường `exchangeForLongLived=true`).
- `POST /v1/api/marketing/config/facebook/token/refresh` - Làm mới page access token bằng long-lived user token đã lưu (chỉ hoạt động nếu đã lưu long-lived token).

### Facebook Marketing Publish

- `POST /v1/api/marketing/facebook/publish` - Đăng bài lên Facebook Page.
- `GET /v1/api/marketing/facebook/pages` - Lấy thông tin Facebook Page đã kết nối.

### CMS Custom Domains

- `POST /v1/api/cms/domains` - Đăng ký tên miền tuỳ chỉnh.
- `GET /v1/api/cms/domains` - Lấy danh sách tên miền.
- `POST /v1/api/cms/domains/{domainId}/verify` - Xác thực sở hữu tên miền qua DNS TXT.
- `POST /v1/api/cms/domains/{domainId}/dns-check` - Kiểm tra phân giải DNS A/CNAME.
- `PATCH /v1/api/cms/domains/{domainId}/activate` - Kích hoạt tên miền đã xác thực.

### CMS Content Publishing

- `POST /v1/api/cms/contents` - Tạo nội dung CMS mới.
- `POST /v1/api/cms/contents/{contentId}/publish-domain` - Xuất bản nội dung lên tên miền tuỳ chỉnh.
- `GET /v1/api/cms/contents/{contentId}/public-url` - Lấy URL public của nội dung.

### CMS Public Route (Unauthenticated)

- `GET /p/{slug}/{contentId}` - Truy xuất và render nội dung CMS (truyền `Host` header khớp với tên miền đã kích hoạt).

## #Bắt đầu


### 1. Khởi tạo Database (Docker)

```bash
docker-compose up -d
```
Khởi tạo container MySQL chạy tại cổng 3307.

### 1. Build project

```bash
mvn clean install
```

### 2. Run application

```bash
cd Host
mvn spring-boot:run
```

Sau khi server chạy, API có thể được truy cập tại:
`http://localhost:8080`

Test api:
`http://localhost:8080/v1/api/auth/status`

Swagger UI:
`http://localhost:8080/swagger/index.html`

## #Config

Các cấu hình của ứng dụng được quản lý tại:
`Host/src/main/resources/application.properties`

Mặc định hiện tại:
```properties
spring.application.name=marketing-backend
server.port=8080 // default port
```

Các biến môi trường chính cần có gồm:
- `APP_NAME`
- `SERVER_PORT`
- `HOST_URL`
- `HOST_NAME`
- `HOST_PASSWORD`
- `JWT_SECRET_KEY`
- `JWT_EXPIRATION_TIME`
- `JWT_REFRESH_EXPIRATION_TIME`
- `MAIL_HOST`
- `MAIL_PORT`
- `MAIL_USERNAME`
- `MAIL_APP_PASSWORD`
- `FACEBOOK_APP_ID`
- `FACEBOOK_APP_SECRET`
- `FACEBOOK_TOKEN_ENCRYPTION`

## #Facebook Marketing Flow

### Luồng kết nối Facebook

```
1. Lấy User Access Token từ Facebook Graph API Explorer
   https://developers.facebook.com/tools/explorer/

2. Lấy Page ID từ Facebook Page Settings

3. Call API lưu token:
   PUT /v1/api/marketing/config/facebook/token
   Body: {
     "accessToken": "USER_ACCESS_TOKEN",
     "pageId": "YOUR_PAGE_ID",
     "exchangeForLongLived": true
   }

4. Kiểm tra trạng thái:
   GET /v1/api/marketing/config/facebook/token/status
```

### Luồng đăng bài

```
POST /v1/api/marketing/facebook/publish
Body: {
  "message": "Nội dung bài đăng"
}
```

### Token Flow

```
Short-lived Token (1-2h) 
    ↓ exchangeForLongLived=true
Long-lived User Token (60 days) ← lưu để auto-refresh
    ↓
Page Access Token (never expires) ← dùng để đăng bài
```

## #Custom Domain & CMS Publishing Flow

### Luồng quản lý tên miền

1. **Đăng ký tên miền**: Client gọi API đăng ký tên miền. Hệ thống trả về một mã token (verification token) để cấu hình bản ghi TXT.
2. **Cấu hình DNS (TXT)**: Người dùng lên trình quản lý DNS của tên miền, thêm bản ghi TXT với giá trị token nhận được để xác minh quyền sở hữu.
3. **Xác minh (Verify)**: Gọi API verify. Backend sẽ query DNS của tên miền để tìm bản ghi TXT. Nếu khớp, tên miền chuyển sang trạng thái `VERIFIED`.
4. **Cấu hình DNS (A/CNAME)**: Người dùng trỏ tên miền về IP/CNAME của hệ thống.
5. **Kiểm tra DNS (DNS Check)**: Gọi API dns-check. Nếu phân giải thành công về hệ thống, tên miền chuyển sang trạng thái `POINTED`.
6. **Kích hoạt (Activate)**: Gọi API activate. Tên miền chuyển sang `ACTIVE` và sẵn sàng sử dụng.

### Luồng xuất bản nội dung

1. **Tạo nội dung**: Đăng bài CMS mới và chuyển trạng thái sang `PUBLISHED`.
2. **Xuất bản lên tên miền**: Chọn bài viết, gắn slug, ảnh bìa, tóm tắt và xuất bản lên một tên miền `ACTIVE`. Hệ thống sẽ sinh ra public URL tương ứng.

### Cách test Custom Domain API

Sử dụng file `custom_domain.http` đính kèm trong dự án:

1. Copy token đăng nhập vào biến `@token`.
2. Chạy request **1.1 Register a new custom domain** (Có thể thay đổi tên miền trong body).
3. (Test Local) Cập nhật trạng thái thủ công trong database nếu không có tên miền thật:
   ```sql
   UPDATE custom_domains SET verification_status = 'VERIFIED' WHERE domain_id = 'YOUR_DOMAIN_ID';
   UPDATE custom_domains SET dns_status = 'POINTED' WHERE domain_id = 'YOUR_DOMAIN_ID';
   ```
4. Chạy request **1.5 Activate the Domain**.
5. Chạy request **2.1 First, create a CMS Content piece to publish** để tạo bài viết.
6. Chạy request **2.2 Publish the Content to the Custom Domain**.
7. Chạy request **3.1 Fetch the published page via the Public Route** với Header `Host: your-domain.com` để xem nội dung public trả về.

