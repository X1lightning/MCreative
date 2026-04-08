# Portfolio Starter App

Starter application for a self-hosted graphic design portfolio website built with Spring Boot, PostgreSQL, Vue 3, Vite, and TypeScript. The design is intentionally simple, image-first, and suited to modest hardware such as a Raspberry Pi.

## Stack

- Backend: Java 21, Spring Boot 3.x, Gradle 8.x
- Frontend: Vue 3, Vite, TypeScript, Pinia, Vue Router
- Database: PostgreSQL
- Image storage: local disk under a configurable uploads directory
- Auth: session-based Spring Security for a single-admin workflow

## Production approach

This starter uses a single-process deployment model for phase 1:

- Vue builds into `src/main/resources/static`
- Spring Boot serves the SPA, REST API, and uploaded images

Why this approach:

- Simpler deployment on a Raspberry Pi
- Only one Java process to manage
- No nginx requirement for the first version

Alternative:

- You can also run the Vue app separately behind nginx and proxy `/api` to Spring Boot
- That approach is useful once you want independent frontend deploys, caching rules, TLS termination, or multiple apps on one box

## Project layout

```text
.
├── build.gradle
├── frontend/
├── gradle/
├── src/
│   ├── main/
│   │   ├── java/com/example/portfolio/
│   │   └── resources/
│   └── test/
└── README.md
```

Backend package layout:

```text
com.example.portfolio
  config
  auth
  controller
  service
  repository
  model
    entity
    dto
  mapper
  validation
  exception
  util
```

## Features in this starter

- Public pages for home, portfolio, project detail, about, and contact
- Public REST endpoints for site info, published projects, categories, and project detail
- Admin login using Spring Security sessions
- Admin CRUD for projects
- Admin image upload, alt text updates, reorder, delete, and cover image selection
- Flyway migrations for schema and sample portfolio content
- Seeded admin creation from environment variables or application properties

## Image storage

Uploaded files are stored on disk, not in PostgreSQL.

Expected folder layout:

```text
uploads/
  projects/
    northline-coffee-identity/
      cover-550e8400-e29b-41d4-a716-446655440000.jpg
      detail-42fa65a5-ded8-4a79-98dc-86f250d990d0.jpg
```

Notes:

- DB stores the relative file path such as `projects/northline-coffee-identity/cover-uuid.jpg`
- Files are exposed by Spring Boot under `/uploads/...`
- File names are generated with a prefix plus UUID to avoid collisions
- Allowed upload types: JPG, JPEG, PNG
- File size limit is configurable
- Deleting a single image removes both DB metadata and the file from disk
- Deleting a project removes image metadata via DB cascade, but does not remove the project folder from disk automatically

That last rule is deliberate for safety. It avoids deleting files unexpectedly if you later share a storage path across environments or apps.

## Configuration

Primary config is in [application.yml](/Users/jasoncrossman/CursorDevelopment/x1/c1/MCreative/src/main/resources/application.yml).

Important environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `APP_UPLOAD_DIR`
- `APP_ALLOWED_ORIGINS`
- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`
- `APP_SEED_ENABLED`
- `APP_SITE_NAME`
- `APP_DESIGNER_NAME`
- `APP_TAGLINE`
- `APP_ABOUT_SNIPPET`
- `APP_CONTACT_EMAIL`
- `APP_LINKEDIN_URL`
- `APP_INSTAGRAM_URL`

Example:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/portfolio
export DB_USERNAME=portfolio
export DB_PASSWORD=portfolio
export APP_UPLOAD_DIR=/opt/portfolio/uploads
export APP_ADMIN_USERNAME=admin
export APP_ADMIN_PASSWORD='replace-this'
export APP_ALLOWED_ORIGINS=http://localhost:5173
```

## Local setup

### 1. PostgreSQL

Create a database and user:

```sql
create user portfolio with password 'portfolio';
create database portfolio owner portfolio;
```

### 2. Backend

Run the Spring Boot app:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The API will be available at `http://localhost:8080`.

### 3. Frontend

From the frontend directory:

```bash
cp .env.example .env
npm install
npm run dev
```

The SPA will run at `http://localhost:5173` and call the backend at `http://localhost:8080` by default.

### 4. Production build

Build the frontend into Spring Boot static resources:

```bash
cd frontend
npm install
npm run build
cd ..
./gradlew clean bootJar
```

Then run the jar:

```bash
java -jar build/libs/portfolio-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Raspberry Pi notes

- Use a 64-bit Raspberry Pi OS if possible
- Keep the image upload directory outside the app jar, for example `/srv/portfolio/uploads`
- Run PostgreSQL separately and keep its memory settings modest
- Use `systemd` to manage the Spring Boot jar
- If another Spring Boot app already runs on the Pi, choose a different port such as `8081`
- Put both apps behind a reverse proxy later if you want clean public URLs

Example:

```bash
java -jar portfolio.jar --server.port=8081 --spring.profiles.active=prod
```

## Development vs production

- `dev` profile: sample admin seed enabled by default, CORS for Vite, separate frontend dev server
- `prod` profile: seed disabled by default, same code path, frontend expected to be built and served by Spring Boot

## Database migrations

- [V1__init_schema.sql](/Users/jasoncrossman/CursorDevelopment/x1/c1/MCreative/src/main/resources/db/migration/V1__init_schema.sql) creates tables, constraints, and indexes
- [V2__seed_sample_projects.sql](/Users/jasoncrossman/CursorDevelopment/x1/c1/MCreative/src/main/resources/db/migration/V2__seed_sample_projects.sql) inserts sample projects and image metadata

## REST API overview

Public endpoints:

- `GET /api/public/site`
- `GET /api/public/projects?category=Brand%20Identity&featured=true`
- `GET /api/public/projects/{slug}`
- `GET /api/public/categories`

Admin auth endpoints:

- `POST /api/admin/auth/login`
- `POST /api/admin/auth/logout`
- `GET /api/admin/auth/me`

Admin project endpoints:

- `GET /api/admin/projects`
- `GET /api/admin/projects/{id}`
- `POST /api/admin/projects`
- `PUT /api/admin/projects/{id}`
- `DELETE /api/admin/projects/{id}`

Admin image endpoints:

- `POST /api/admin/projects/{projectId}/images`
- `PUT /api/admin/projects/{projectId}/images/{imageId}`
- `DELETE /api/admin/projects/{projectId}/images/{imageId}`
- `PUT /api/admin/projects/{projectId}/images/reorder`
- `PUT /api/admin/projects/{projectId}/images/{imageId}/set-cover`

## Example payloads

### `POST /api/admin/auth/login`

Request:

```json
{
  "username": "admin",
  "password": "change-me-now"
}
```

Response:

```json
{
  "message": "Login successful",
  "user": {
    "id": 1,
    "username": "admin",
    "authenticated": true
  }
}
```

### `POST /api/admin/projects`

Request:

```json
{
  "title": "Northline Coffee Identity",
  "slug": "northline-coffee-identity",
  "category": "Brand Identity",
  "description": "A warm identity system for a student-run coffee concept.",
  "toolsUsed": "Adobe Illustrator, InDesign",
  "createdFor": "Concept Studio Brief",
  "projectDate": "2025-10-14",
  "featured": true,
  "published": true,
  "sortOrder": 1
}
```

Response:

```json
{
  "id": 4,
  "title": "Northline Coffee Identity",
  "slug": "northline-coffee-identity",
  "category": "Brand Identity",
  "description": "A warm identity system for a student-run coffee concept.",
  "toolsUsed": "Adobe Illustrator, InDesign",
  "createdFor": "Concept Studio Brief",
  "projectDate": "2025-10-14",
  "featured": true,
  "published": true,
  "sortOrder": 1,
  "coverImage": null,
  "images": []
}
```

### `POST /api/admin/projects/{projectId}/images`

Request:

- `multipart/form-data`
- field `file`: JPG, JPEG, or PNG
- field `altText`: optional text

Response:

```json
{
  "id": 12,
  "url": "/uploads/projects/northline-coffee-identity/cover-550e8400-e29b-41d4-a716-446655440000.jpg",
  "fileName": "cover-550e8400-e29b-41d4-a716-446655440000.jpg",
  "originalFileName": "northline-cover.jpg",
  "altText": "Coffee bag mockup with wordmark",
  "displayOrder": 0,
  "cover": true
}
```

### `PUT /api/admin/projects/{projectId}/images/reorder`

Request:

```json
{
  "items": [
    { "imageId": 12, "displayOrder": 0 },
    { "imageId": 13, "displayOrder": 1 }
  ]
}
```

### `GET /api/public/projects/{slug}`

Response:

```json
{
  "id": 1,
  "title": "Northline Coffee Identity",
  "slug": "northline-coffee-identity",
  "category": "Brand Identity",
  "description": "A warm, editorial brand system for a student-run coffee concept, spanning packaging, menu layouts, and social launch graphics.",
  "toolsUsed": "Adobe Illustrator, Adobe InDesign, Figma",
  "createdFor": "Concept Studio Brief",
  "projectDate": "2025-10-14",
  "featured": true,
  "published": true,
  "sortOrder": 1,
  "coverImage": {
    "id": 1,
    "url": "/uploads/projects/northline-coffee-identity/cover-demo-1.jpg",
    "fileName": "cover-demo-1.jpg",
    "originalFileName": "northline-cover.jpg",
    "altText": "Coffee bag mockup with wordmark",
    "displayOrder": 0,
    "cover": true
  },
  "images": [
    {
      "id": 1,
      "url": "/uploads/projects/northline-coffee-identity/cover-demo-1.jpg",
      "fileName": "cover-demo-1.jpg",
      "originalFileName": "northline-cover.jpg",
      "altText": "Coffee bag mockup with wordmark",
      "displayOrder": 0,
      "cover": true
    },
    {
      "id": 2,
      "url": "/uploads/projects/northline-coffee-identity/detail-demo-1.jpg",
      "fileName": "detail-demo-1.jpg",
      "originalFileName": "northline-menu.jpg",
      "altText": "Folded menu layout",
      "displayOrder": 1,
      "cover": false
    }
  ]
}
```

## Security notes

- This starter uses session-based auth because it is the simplest maintainable option for a single-admin site
- The initial admin is created on startup from properties if it does not already exist
- Change the default admin password before deploying

## Known operational note

The sample seed data inserts metadata paths for demo images, but it does not include real image files. Add your own optimized JPGs under the configured uploads directory to match those sample records, or delete the sample rows and upload fresh work through the admin UI.

## Future enhancements

- Drag-and-drop image reordering
- Contact form submission backend
- Automatic image optimization and variants
- Tags and collections
- Editable site settings in admin
- Resume upload/download
