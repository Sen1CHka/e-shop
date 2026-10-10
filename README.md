# E-shop

An online electronics store built as a semester project for the NUR course (CTU in Prague).
Customers browse the catalogue, fill a cart, order and follow their orders; administrators run the shop.

- **Backend** – Java 17, Spring Boot 3, Spring Security (JWT), JPA, H2 / PostgreSQL
- **Frontend** – Angular 17 (standalone components, signals), Nx workspace, Bootstrap 5

## Contents

1. [Quick start](#quick-start)
2. [What the shop can do](#what-the-shop-can-do)
3. [User roles](#user-roles)
4. [User stories](#user-stories)
5. [Data model](#data-model)
6. [REST API](#rest-api)
7. [Project structure](#project-structure)
8. [UI design decisions](#ui-design-decisions)
9. [Tests](#tests)
10. [Plan and backlog](#plan-and-backlog)

## Quick start

Requirements: **JDK 17–21** and **Node.js 18+**. No database has to be installed.

```bash
# 1. Backend – http://localhost:8090 (Swagger UI: http://localhost:8090/swagger-ui.html)
./gradlew bootRun            # Windows: gradlew.bat bootRun

# 2. Frontend – http://localhost:4200
cd selling-frontend
npm install
npm start
```

> Gradle 8.5 does not run on JDK 22 or newer. If `java -version` shows a newer one,
> point `JAVA_HOME` to JDK 17 before running Gradle.

On the first start the database is filled with demo data (25 products, 6 categories, orders, reviews).

| Demo account  | Username | Password   |
|---------------|----------|------------|
| Customer      | `jana`   | `jana123`  |
| Customer      | `petr`   | `petr123`  |
| Administrator | `admin`  | `admin123` |

**Database.** By default the data lives in an embedded H2 file in `./data`. Delete that folder to start
again with fresh demo data (handy before a usability test). To use PostgreSQL instead:

```bash
docker compose up -d
./gradlew bootRun --args="--spring.profiles.active=postgres"
```

## What the shop can do

**Storefront**
- Home page with categories, new arrivals and cheap picks
- Catalogue with full-text search, category / price / availability filters, sorting and paging;
  every result has its own address, so it can be bookmarked or shared and the Back button works
- Product page with stock status, reviews and related products
- Cart that works without signing in and survives closing the browser
- Three-step checkout (address → delivery and payment → summary) with saved addresses
- Customer account: order history, order tracking, cancelling and re-ordering, favourites,
  address book, profile and password change

**Administration** (`/admin`)
- Dashboard: revenue, orders by state, best sellers, products running low
- Orders: filtering by state, customer and date, detail, state changes
- Products and categories: create, edit, hide from the shop
- Users: roles and removal

## User roles

| Role              | Who it is                          | What they can do                                           |
|-------------------|------------------------------------|------------------------------------------------------------|
| **Visitor**       | Anybody without an account         | Browse, search, fill the cart, register                    |
| **Customer**      | Signed-in user (`USER`)            | Everything above + order, reviews, favourites, account     |
| **Administrator** | Shop staff (`ADMIN`)               | Everything above + administration of the whole shop        |

## REST API

Full interactive documentation: <http://localhost:8090/swagger-ui.html> (use *Authorize* with the token
returned by the login).

| Area | Endpoints | Access |
|------|-----------|--------|
| Authentication | `POST /api/auth/register`, `POST /api/auth/login` | public |
| Profile | `GET/PUT /api/auth/me`, `PUT /api/auth/me/password` | signed in |
| Catalogue | `GET /api/products` (`q`, `categoryId`, `minPrice`, `maxPrice`, `inStock`, `sort`, `page`, `size`), `GET /api/products/{id}`, `GET /api/products/batch`, `GET /api/categories` | public |
| Reviews | `GET /api/products/{id}/reviews` · `POST /api/products/{id}/reviews`, `DELETE /api/reviews/{id}` | public · signed in |
| Checkout | `GET /api/checkout/options` · `POST /api/orders` | public · signed in |
| My orders | `GET /api/orders`, `GET /api/orders/{id}`, `POST /api/orders/{id}/cancel` | signed in (own orders) |
| Favourites | `GET /api/favorites`, `PUT/DELETE /api/favorites/{productId}` | signed in |
| Addresses | `GET/POST /api/addresses`, `PUT/DELETE /api/addresses/{id}` | signed in (own addresses) |
| Catalogue management | `POST/PUT/DELETE /api/products/**`, `POST/PUT/DELETE /api/categories/**` | administrator |
| Administration | `GET /api/admin/stats`, `GET /api/admin/products`, `GET /api/admin/orders` (`state`, `user`, `dateTo`), `PATCH /api/admin/orders/{id}/state`, `GET /api/admin/users`, `PATCH /api/admin/users/{id}/role`, `DELETE /api/admin/users/{id}` | administrator |

Errors always have the same shape, so the frontend can show them next to the right field:

```json
{ "status": 400, "message": "Price must be positive", "fieldErrors": { "price": "Price must be positive" } }
```

## Project structure

```
src/main/java/eshop
├── configuration   security (JWT, CORS, roles), OpenAPI, demo data
├── controller      REST endpoints
├── contracts       request / response objects (records) with validation
├── domain          JPA entities and enums
├── exception       ApiException and one global error handler
├── repository      Spring Data repositories
└── service         business rules (stock, order states, ownership checks)

selling-frontend
├── apps/selling-frontend   application shell, routes, global styles (design tokens)
└── libs
    ├── domain    models, API services, auth, cart, guards, interceptor
    ├── shared    reusable UI: product card, dialog, toasts, rating, form field, pagination...
    ├── shop      storefront pages: home, catalogue, product, cart, checkout, account
    └── selling   administration pages: dashboard, orders, products, categories, users
```

## UI design decisions

Short notes on *why* the interface behaves the way it does – useful for the NUR report.

- **Visibility of system status** – every action answers with a toast; lists have loading skeletons,
  empty states with a next step and a retry on failure; the cart badge shows the count and the price.
- **Error prevention** – a sold-out product cannot be added; quantity is limited by the stock; forms validate
  before sending; destructive actions ask for confirmation in the user's words ("Cancel order" / "Keep order").
- **Undo instead of confirm** for cheap mistakes – removing an item from the cart can simply be taken back.
- **Recognition over recall** – active filters are visible as chips; the checkout summary repeats every
  choice with a *Change* link; saved addresses are offered instead of typed.
- **No forced registration** – the cart works for anonymous visitors; sign-in is asked for only at checkout
  and returns the user to where they were.
- **Stable layout** – room for validation messages is reserved, so a message appearing when a field loses
  focus never moves the button the user is about to click.
- **Accessibility** – semantic landmarks and headings, labels on all inputs, visible keyboard focus,
  skip link, dialogs closable with Esc, `aria-live` toasts, colour contrast of at least 4.5:1,
  state is never communicated by colour alone, `prefers-reduced-motion` is respected.
- **Responsive** – from 360 px wide phones (filters become a full-screen panel, tables scroll) to desktop.

## Tests

```bash
./gradlew test                              # backend: repositories, order rules, security
cd selling-frontend
npx nx run-many -t test                     # frontend unit tests (cart, price formatting, app shell)
npx nx run-many -t lint
npx nx e2e selling-frontend-e2e             # Cypress, needs the backend running
```

## Plan and backlog

**Done in this iteration**

1. Data model extended from 3 to 7 tables (categories, order items, addresses, reviews, favourites).
2. Backend rebuilt: working JWT security with roles, validation, uniform errors, stock handling, search.
3. Storefront for customers (it did not exist before) and a reworked administration.
4. Demo data, tests, documentation.

**Next iterations** (ordered by value for the customer; IDs refer to the user stories above)

| Priority | Item | Stories |
|----------|------|---------|
| 1 | E-mail notifications and password reset | US-28, US-29 |
| 2 | Product photo upload and gallery | US-06, US-37 |
| 3 | Online payment | US-17 |
| 4 | Discount codes | US-18 |
| 5 | Product comparison | US-07 |
| 6 | CSV export of orders | US-38 |
| 7 | Czech localisation of the interface | – |

**Suggested usability test tasks** (cover the main flows):

1. Find wireless headphones under 3 000 Kč that are in stock and add them to the cart.
2. Order the cart to a parcel locker and pay on delivery. What is the final price?
3. Find out where your last order is and cancel it.
4. Save a product for later and find it again from a different page.
5. (Administrator) A customer called that their parcel arrived – mark the order as delivered.
6. (Administrator) Product *PowerBank 20000* has been restocked with 30 pieces – update it.
