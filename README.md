# hikmetcakir.com

Personal blog and content platform built with a microservice architecture. Articles are written through a private editor interface and published on the public-facing blog at [hikmetcakir.com](https://hikmetcakir.com).

---

## Architecture

The project is split into independent services, each with a single responsibility.

```
hikmetcakir.com/
├── core-api        # Main REST API — articles, categories, auth
├── analytics-api   # View tracking and analytics
├── config-api      # Centralized configuration service
├── ui-reader       # Public blog (Astro) — hikmetcakir.com
├── ui-editor       # Private CMS (Astro) — article management
└── scripts         # Backup and maintenance scripts
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot |
| Database | MongoDB |
| Frontend | Astro, JavaScript, CSS |
| Infrastructure | Docker, Nginx, DigitalOcean |
| CI/CD | GitHub Actions |

---

## Services

### core-api
The central backend service. Handles article CRUD, category management, and serves data to the UI Reader. Built with Spring Boot and MongoDB.

### analytics-api
Tracks article views and user interactions. Separated from core-api to keep concerns isolated and allow independent scaling.

### config-api
Spring Cloud Config server. Manages environment-specific configuration across services.

### ui-reader
The public blog. Built with Astro for fast static rendering with server-side rendering for dynamic content. Features infinite scroll, category filtering, syntax-highlighted code blocks, and a responsive layout.

### ui-editor
A private CMS for writing and publishing articles. Uses Quill.js as the rich text editor with support for code blocks, images, and category assignment. Only accessible internally.

---

## CI/CD

Every push to `main` triggers a GitHub Actions workflow that:

1. Builds and pushes Docker images
2. Deploys to DigitalOcean via SSH
3. Restarts the relevant services through Docker Compose

An AI-assisted issue workflow is also configured — opening a GitHub issue automatically creates a branch, applies the relevant code changes, and opens a pull request for review.

---

## Local Setup

```bash
# Clone
git clone https://github.com/hikmet-cakir/hikmetcakir.com.git
cd hikmetcakir.com

# Start all services
docker-compose up -d
```

Each service can also be run independently.

**Prerequisites:** Java 17+, Node.js 18+, Docker, MongoDB

---

## License

MIT
