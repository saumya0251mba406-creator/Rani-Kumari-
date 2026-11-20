# Riwaayat — Regional Indian Fashion Discovery Prototype

**Tagline:** *Indian Heritage. Your Everyday Style.*

An Android Jetpack Compose prototype built for the DGU Venture Quest college entrepreneurship assignment by Rani Kumari.

## How to Run

1. Open the project in Android Studio or run via Gradle in this workspace:
   - Build debug APK: `gradle :app:assembleDebug`
   - Run unit & Robolectric tests: `gradle :app:testDebugUnitTest`
2. No API keys, Firebase project, paid services, or user sign-in are required.
3. All product data is bundled locally; wishlist, shopping bag, non-sensitive demo enquiry snapshots, and optional store WhatsApp configuration are persisted locally in `SharedPreferences`.

## Implemented Features

- **Home:** Venture hero, Shop by Region (5 planned regions), Featured Clothing, How Riwaayat Works, Why Riwaayat, and Artisan & Supplier Intention note.
- **Explore / Product Catalogue:** Combined search (name, region, category), filters (region, category, price range), numeric price & name sorting, live filtered count, clear-all-filters, and empty state.
- **Product Details:** Textile-inspired bespoke artwork with safe fallback, size selection (for Kurtas & Sets; `"One Size"` for Dupattas & Stoles), color swatches, variant-specific demo stock validation, quantity selector, Add to Bag feedback, Wishlist toggle, illustrative Materials & Care, Design Inspiration & Origin status, Delivery & Returns notes, and Related Products.
- **Regional Stories:** Story cards for Bihar, Uttar Pradesh, Punjab, Uttarakhand, and Telangana with cautious design notes, linked sample products, `"Shop this region"` deep-filter CTA, and accessible `"Read Story Details"` modal.
- **Wishlist & Shopping Bag:** Persistent wishlist without login; variant-keyed (`productId + size + color`) bag merging, stock limits, line subtotals, and `"To be confirmed"` shipping summary using `en-IN` INR formatting.
- **Order-Enquiry Form & Confirmation:** In-memory validated enquiry form (Full name, Indian mobile with optional `+91`, City, State, 6-digit non-zero-leading Pincode, optional Email & note, required prototype acknowledgement), `"Save Demo Enquiry"` (saves non-sensitive snapshot only, clears bag), and `"Send via WhatsApp"` (disabled until configured, with `"Copy Enquiry Message"` fallback).
- **About & Educational Demo Dashboard:** Student venture overview without personal email/ID exposure, live-calculated dashboard metrics (`Estimated enquiry value`), Store WhatsApp configuration, optional `"Load sample enquiries"` (`Sample data` badge), enquiry detail modal, and confirmed `"Reset Demo Data"`.

## Prototype Limitations

- Sample catalogue items, prices, stock counts, and textile illustrations are demonstration data only.
- No live payments, shipping carrier integrations, or verified artisan/supplier origin certifications are implemented.
- WhatsApp opens a prepared message in an external client when configured; the customer must send it manually.
- Demo Dashboard is an educational local view and not a secure multi-user admin backend.

## Presentation Demo Script (College Assignment)

1. Launch **Home** and point out the sitewide prototype notice and `"Wear a Story. Celebrate a Region."` hero.
2. Tap **Bihar** under *Shop by Region* to open **Explore** pre-filtered to Bihar.
3. Open **Mithila-Inspired Cotton Kurta**, inspect materials/origin status, choose size `M` and color `Warm Ivory`, and tap **Add to Bag**.
4. Tap **View Bag**, increase quantity to `2`, verify the updated **Estimated merchandise total**, and tap **Proceed to Enquiry**.
5. Fill out the enquiry form (note validation if pincode starts with `0` or checkbox is unchecked), show the unconfigured WhatsApp fallback (**Copy Enquiry Message**), and tap **Save Demo Enquiry**.
6. On **Demo Enquiry Saved**, tap **View Demo Dashboard** to show the saved non-sensitive record, metrics, and Store WhatsApp configuration.
