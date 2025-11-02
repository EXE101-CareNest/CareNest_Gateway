# Dockerfile ở root để Koyeb có thể tìm thấy
# Build context sẽ là thư mục ApiGateway

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copy project file và restore dependencies
COPY ApiGateway/ApiGateway.csproj ./ApiGateway/
WORKDIR /src/ApiGateway
RUN dotnet restore

# Copy source code
WORKDIR /src
COPY ApiGateway/ ./ApiGateway/

# Build application
WORKDIR /src/ApiGateway
RUN dotnet build "ApiGateway.csproj" -c Release -o /app/build

# Publish application
FROM build AS publish
RUN dotnet publish "ApiGateway.csproj" -c Release -o /app/publish /p:UseAppHost=false

# Runtime stage
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app

# Install bash và sed cho entrypoint script
RUN apt-get update && apt-get install -y bash sed && rm -rf /var/lib/apt/lists/*

# Copy published app
COPY --from=publish /app/publish .

# Copy entrypoint script
COPY ApiGateway/docker-entrypoint.sh /app/
RUN chmod +x /app/docker-entrypoint.sh

# Expose port 80
EXPOSE 80

# Set entry point
ENTRYPOINT ["/app/docker-entrypoint.sh"]

