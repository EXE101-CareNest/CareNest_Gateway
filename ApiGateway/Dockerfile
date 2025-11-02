# Use the official .NET 8.0 SDK image to build the app
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

# Copy the project file and restore dependencies
COPY ["ApiGateway.csproj", "./"]
RUN dotnet restore "./ApiGateway.csproj"

# Copy the rest of the source code
COPY . .

# Build the application
RUN dotnet build "ApiGateway.csproj" -c Release -o /app/build

# Publish the application
FROM build AS publish
RUN dotnet publish "ApiGateway.csproj" -c Release -o /app/publish /p:UseAppHost=false

# Use the official .NET 8.0 ASP.NET runtime image
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app

# Install bash và sed cho entrypoint script
RUN apt-get update && apt-get install -y bash sed && rm -rf /var/lib/apt/lists/*

# Copy the published app
COPY --from=publish /app/publish .

# Copy entrypoint script
COPY docker-entrypoint.sh /app/
RUN chmod +x /app/docker-entrypoint.sh

# Expose port 80
EXPOSE 80

# Set the entry point
ENTRYPOINT ["/app/docker-entrypoint.sh"]