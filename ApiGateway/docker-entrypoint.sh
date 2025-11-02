#!/bin/bash
set -e

# Script để thay thế các biến môi trường trong ocelot.json
if [ -f /app/ocelot.json ]; then
    echo "Đang thay thế các biến môi trường trong ocelot.json..."
    
    # Thay thế các biến môi trường
    sed -i "s|#{ADDRESS_SERVICE_HOST}|${ADDRESS_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{ADDRESS_SERVICE_PORT}|${ADDRESS_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{APPOINTMENT_SERVICE_HOST}|${APPOINTMENT_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{APPOINTMENT_SERVICE_PORT}|${APPOINTMENT_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{APPOINTMENTDETAIL_SERVICE_HOST}|${APPOINTMENTDETAIL_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{APPOINTMENTDETAIL_SERVICE_PORT}|${APPOINTMENTDETAIL_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{BRANCHES_SERVICE_HOST}|${BRANCHES_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{BRANCHES_SERVICE_PORT}|${BRANCHES_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{ORDER_SERVICE_HOST}|${ORDER_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{ORDER_SERVICE_PORT}|${ORDER_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{ORDERDETAIL_SERVICE_HOST}|${ORDERDETAIL_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{ORDERDETAIL_SERVICE_PORT}|${ORDERDETAIL_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{PRODUCTS_SERVICE_HOST}|${PRODUCTS_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{PRODUCTS_SERVICE_PORT}|${PRODUCTS_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{PAY_SERVICE_HOST}|${PAY_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{PAY_SERVICE_PORT}|${PAY_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{SERVICE_SERVICE_HOST}|${SERVICE_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{SERVICE_SERVICE_PORT}|${SERVICE_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{SERVICECATEGORY_SERVICE_HOST}|${SERVICECATEGORY_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{SERVICECATEGORY_SERVICE_PORT}|${SERVICECATEGORY_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{SERVICEDETAIL_SERVICE_HOST}|${SERVICEDETAIL_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{SERVICEDETAIL_SERVICE_PORT}|${SERVICEDETAIL_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{SHOP_SERVICE_HOST}|${SHOP_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{SHOP_SERVICE_PORT}|${SHOP_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{REVIEW_SERVICE_HOST}|${REVIEW_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{REVIEW_SERVICE_PORT}|${REVIEW_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{AUTH_SERVICE_HOST}|${AUTH_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{AUTH_SERVICE_PORT}|${AUTH_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{PET_SERVICE_HOST}|${PET_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{PET_SERVICE_PORT}|${PET_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{BLOG_SERVICE_HOST}|${BLOG_SERVICE_HOST:-localhost}|g" /app/ocelot.json
    sed -i "s|#{BLOG_SERVICE_PORT}|${BLOG_SERVICE_PORT:-80}|g" /app/ocelot.json
    
    sed -i "s|#{GATEWAY_BASE_URL}|${GATEWAY_BASE_URL:-http://localhost:80}|g" /app/ocelot.json
    
    # Thay thế host.docker.internal:8082 (sử dụng cho các service nội bộ)
    # Thay thế cả host và port cùng lúc để tránh thay thế nhầm
    INTERNAL_HOST="${INTERNAL_SERVICE_HOST:-localhost}"
    INTERNAL_PORT="${INTERNAL_SERVICE_PORT:-8082}"
    sed -i "s|\"Host\": \"host.docker.internal\",|\"Host\": \"${INTERNAL_HOST}\",|g" /app/ocelot.json
    sed -i "s|\"Port\": 8082|\"Port\": ${INTERNAL_PORT}|g" /app/ocelot.json
    
    echo "Đã hoàn thành việc thay thế các biến môi trường."
fi

# Chạy ứng dụng
exec dotnet ApiGateway.dll

