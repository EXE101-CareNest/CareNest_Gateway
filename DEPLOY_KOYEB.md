# Hướng dẫn Deploy ApiGateway lên Koyeb

## Yêu cầu trước khi deploy

1. **Tài khoản Koyeb**: Đăng ký tại https://www.koyeb.com
2. **Git Repository**: Code phải được push lên GitHub, GitLab, hoặc Bitbucket
3. **Dockerfile**: Đã được chuẩn bị sẵn trong thư mục `ApiGateway/`

## Các bước deploy

### Bước 1: Push code lên Git Repository

```bash
git add .
git commit -m "Prepare for Koyeb deployment"
git push origin main
```

### Bước 2: Tạo ứng dụng trên Koyeb

1. Đăng nhập vào Koyeb Dashboard
2. Click **"Create App"** hoặc **"New App"**
3. Chọn **"GitHub"**, **"GitLab"**, hoặc **"Bitbucket"** làm source
4. Chọn repository chứa code của bạn
5. Chọn branch (thường là `main` hoặc `master`)

### Bước 3: Cấu hình Build Settings

Trong phần **Build Settings**:
- **Build Command**: Để trống (Koyeb sẽ tự động detect Dockerfile)
- **Dockerfile Path**: `Dockerfile` (file ở root)
- **Hoặc**: Nếu không có file `.koyeb.yaml`, bạn có thể chỉ định:
  - **Dockerfile Path**: `ApiGateway/Dockerfile`
  - **Build Context**: `.` (root của repository)

**Lưu ý**: File `.koyeb.yaml` đã được cấu hình sẵn, Koyeb sẽ tự động sử dụng nó

### Bước 4: Cấu hình Environment Variables (Biến môi trường)

Trong phần **Environment Variables**, thêm các biến sau:

#### Các biến cho các service downstream:

```env
# Address Service
ADDRESS_SERVICE_HOST=your-address-service-host
ADDRESS_SERVICE_PORT=80

# Appointment Service
APPOINTMENT_SERVICE_HOST=your-appointment-service-host
APPOINTMENT_SERVICE_PORT=80

# Appointment Detail Service
APPOINTMENTDETAIL_SERVICE_HOST=your-appointmentdetail-service-host
APPOINTMENTDETAIL_SERVICE_PORT=80

# Branches Service
BRANCHES_SERVICE_HOST=your-branches-service-host
BRANCHES_SERVICE_PORT=80

# Order Service
ORDER_SERVICE_HOST=your-order-service-host
ORDER_SERVICE_PORT=80

# Order Detail Service
ORDERDETAIL_SERVICE_HOST=your-orderdetail-service-host
ORDERDETAIL_SERVICE_PORT=80

# Products Service
PRODUCTS_SERVICE_HOST=your-products-service-host
PRODUCTS_SERVICE_PORT=80

# Pay Service
PAY_SERVICE_HOST=your-pay-service-host
PAY_SERVICE_PORT=80

# Service Service
SERVICE_SERVICE_HOST=your-service-service-host
SERVICE_SERVICE_PORT=80

# Service Category Service
SERVICECATEGORY_SERVICE_HOST=your-servicecategory-service-host
SERVICECATEGORY_SERVICE_PORT=80

# Service Detail Service
SERVICEDETAIL_SERVICE_HOST=your-servicedetail-service-host
SERVICEDETAIL_SERVICE_PORT=80

# Shop Service
SHOP_SERVICE_HOST=your-shop-service-host
SHOP_SERVICE_PORT=80

# Review Service
REVIEW_SERVICE_HOST=your-review-service-host
REVIEW_SERVICE_PORT=80

# Auth Service
AUTH_SERVICE_HOST=your-auth-service-host
AUTH_SERVICE_PORT=80

# Pet Service
PET_SERVICE_HOST=your-pet-service-host
PET_SERVICE_PORT=80

# Blog Service
BLOG_SERVICE_HOST=your-blog-service-host
BLOG_SERVICE_PORT=80

# Gateway Base URL
GATEWAY_BASE_URL=https://your-app-name.koyeb.app

# Internal Service (cho các route sử dụng host.docker.internal:8082)
INTERNAL_SERVICE_HOST=your-internal-service-host
INTERNAL_SERVICE_PORT=8082
```

**Lưu ý**: 
- Thay thế các giá trị `your-*-service-host` bằng hostname thực tế của các service
- Nếu các service được deploy trên Koyeb, bạn có thể sử dụng internal service discovery hoặc public URLs
- Port thường là `80` cho HTTP hoặc `443` cho HTTPS
- `INTERNAL_SERVICE_HOST` dùng để thay thế `host.docker.internal` trong các route như `/accounts`, `/shops`, `/permission`, etc. Đây thường là service Account hoặc Auth service chạy trên port 8082

### Bước 5: Cấu hình Port

- **Port**: `80` (đã được expose trong Dockerfile)

### Bước 6: Deploy

1. Click **"Deploy"** hoặc **"Save and Deploy"**
2. Koyeb sẽ tự động build Docker image và deploy ứng dụng
3. Quá trình này có thể mất vài phút

### Bước 7: Kiểm tra

1. Sau khi deploy thành công, bạn sẽ nhận được một URL dạng: `https://your-app-name.koyeb.app`
2. Test API Gateway bằng cách gọi các endpoint đã được cấu hình trong `ocelot.json`

## Cấu trúc file

```
ApiGateway/
├── Dockerfile              # Dockerfile để build image
├── docker-entrypoint.sh    # Script để thay thế biến môi trường
├── ocelot.json            # Cấu hình Ocelot Gateway
├── Program.cs             # Entry point của ứng dụng
└── ...

.koyeb.yaml                # Cấu hình deploy cho Koyeb (optional)
DEPLOY_KOYEB.md           # File hướng dẫn này
```

## Troubleshooting

### Lỗi: Container không start được

- Kiểm tra logs trong Koyeb Dashboard
- Đảm bảo tất cả các biến môi trường đã được cấu hình đúng
- Kiểm tra xem các service downstream có accessible không

### Lỗi: Không kết nối được đến các service

- Kiểm tra hostname và port của các service
- Nếu các service cũng chạy trên Koyeb, sử dụng internal service discovery
- Đảm bảo các service đã được deploy và running

### Lỗi: Build failed

- Kiểm tra Dockerfile có đúng syntax không
- Đảm bảo các file cần thiết đã được commit vào Git
- Kiểm tra build logs trong Koyeb Dashboard

## Tùy chọn: Sử dụng Koyeb Internal Service Discovery

Nếu các service khác cũng được deploy trên Koyeb, bạn có thể sử dụng internal service discovery:

```
ADDRESS_SERVICE_HOST=address-service.koyeb
```

Trong đó `address-service` là tên ứng dụng Koyeb của service đó.

## Tài liệu tham khảo

- [Koyeb Documentation](https://www.koyeb.com/docs)
- [Ocelot Documentation](https://ocelot.readthedocs.io/)

