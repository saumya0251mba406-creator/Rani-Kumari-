package com.example.data

import com.example.model.DemoEnquiryRecord
import com.example.model.EnquiryItemSnapshot
import com.example.model.Product
import com.example.model.ProductCategory
import com.example.model.ProductColorOption
import com.example.model.ProductVariantKey
import com.example.model.Region
import com.example.model.RegionalStory
import com.example.model.TextileMotif

object ProductCatalog {

  const val SITEWIDE_PROTOTYPE_NOTICE =
    "College prototype — sample products and prices. No live payments."

  const val SPECIFICATION_NOTE = "Sample specification for prototype demonstration."

  const val DELIVERY_COPY =
    "Delivery availability, shipping charges, and dispatch timing will be confirmed before payment."

  const val RETURNS_COPY =
    "This prototype does not establish a final return policy. Product-specific exchange and return terms must be confirmed before a real purchase."

  private val standardKurtaSizes = listOf("XS", "S", "M", "L", "XL")
  private val fusionSetSizes = listOf("S", "M", "L", "XL")

  private fun buildStockMap(
    sizes: List<String>,
    colors: List<ProductColorOption>,
    defaultStock: Int,
    overrides: Map<ProductVariantKey, Int> = emptyMap(),
  ): Map<ProductVariantKey, Int> {
    val map = LinkedHashMap<ProductVariantKey, Int>()
    for (size in sizes) {
      for (color in colors) {
        val key = ProductVariantKey(size = size, color = color.name)
        map[key] = overrides[key] ?: defaultStock
      }
    }
    return map
  }

  val products: List<Product> =
    listOf(
      Product(
        id = "mithila-cotton-kurta",
        name = "Mithila-Inspired Cotton Kurta",
        region = Region.BIHAR,
        category = ProductCategory.KURTAS,
        priceInr = 2200,
        shortDescription =
          "Everyday straight-cut kurta featuring Mithila-inspired lotus and double-line border motifs.",
        fullDescription =
          "Designed for campus days, festive gatherings, and casual styling, this sample silhouette pairs a breathable everyday cut with fine geometric border artwork inspired by Mithila visual traditions.",
        illustrativeMaterial =
          "Illustrative demo fabric: mid-weight breathable cotton blend with printed border detailing.",
        availableSizes = standardKurtaSizes,
        availableColors =
          listOf(
            ProductColorOption("Warm Ivory", 0xFFF3ECE0),
            ProductColorOption("Deep Maroon", 0xFF6B2346),
            ProductColorOption("Indigo Slate", 0xFF2F4159),
          ),
        variantStock =
          buildStockMap(
            sizes = standardKurtaSizes,
            colors =
              listOf(
                ProductColorOption("Warm Ivory", 0xFFF3ECE0),
                ProductColorOption("Deep Maroon", 0xFF6B2346),
                ProductColorOption("Indigo Slate", 0xFF2F4159),
              ),
            defaultStock = 4,
            overrides =
              mapOf(
                ProductVariantKey("XS", "Indigo Slate") to 0,
                ProductVariantKey("M", "Warm Ivory") to 5,
                ProductVariantKey("L", "Deep Maroon") to 3,
              ),
          ),
        careInstructions =
          "Illustrative care: gentle cold hand wash separately; dry in shade; warm iron on reverse.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration = "Design inspiration: Bihar (Mithila-inspired line motifs)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Mithila-Inspired Cotton Kurta showing lotus and double-line border patterns.",
        motif = TextileMotif.MITHILA_LOTUS,
        isFeatured = true,
      ),
      Product(
        id = "bhagalpur-woven-dupatta",
        name = "Bhagalpur-Inspired Woven Dupatta",
        region = Region.BIHAR,
        category = ProductCategory.DUPATTAS,
        priceInr = 1800,
        shortDescription =
          "Lightweight textured drape with temple-border stripes inspired by Bhagalpur weaving palettes.",
        fullDescription =
          "A versatile contemporary dupatta intended to pair cleanly with solid kurtas or everyday ethnic-fusion outfits, highlighting warm earthy tones and understated border geometry.",
        illustrativeMaterial =
          "Illustrative demo fabric: textured cotton-viscose slub weave with contrasting selvedge border.",
        availableSizes = listOf(Product.ONE_SIZE),
        availableColors =
          listOf(
            ProductColorOption("Sandstone Gold", 0xFFB68A46),
            ProductColorOption("Plum Wine", 0xFF6B2346),
          ),
        variantStock =
          buildStockMap(
            sizes = listOf(Product.ONE_SIZE),
            colors =
              listOf(
                ProductColorOption("Sandstone Gold", 0xFFB68A46),
                ProductColorOption("Plum Wine", 0xFF6B2346),
              ),
            defaultStock = 6,
            overrides = mapOf(ProductVariantKey(Product.ONE_SIZE, "Plum Wine") to 4),
          ),
        careInstructions =
          "Illustrative care: mild hand wash or dry clean; avoid wringing; steam press gently.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration = "Design inspiration: Bihar (Bhagalpur-inspired woven borders)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Bhagalpur-Inspired Woven Dupatta showing textured slub stripes and temple borders.",
        motif = TextileMotif.BHAGALPUR_WEAVE,
        isFeatured = true,
      ),
      Product(
        id = "banaras-festive-dupatta",
        name = "Banaras-Inspired Festive Dupatta",
        region = Region.UTTAR_PRADESH,
        category = ProductCategory.DUPATTAS,
        priceInr = 2600,
        shortDescription =
          "Occasion-ready dupatta with muted gold lattice patterns inspired by Banarasi festive drapery.",
        fullDescription =
          "Created for college fests, family celebrations, and evening wear, this sample piece balances a rich plum-toned ground with fine metallic-gold inspired jaal motifs.",
        illustrativeMaterial =
          "Illustrative demo fabric: soft art-blend weave with metallic-thread style jacquard motif.",
        availableSizes = listOf(Product.ONE_SIZE),
        availableColors =
          listOf(
            ProductColorOption("Rani Plum", 0xFF6B2346),
            ProductColorOption("Antique Gold", 0xFFB68A46),
            ProductColorOption("Crimson Rose", 0xFF912E3B),
          ),
        variantStock =
          buildStockMap(
            sizes = listOf(Product.ONE_SIZE),
            colors =
              listOf(
                ProductColorOption("Rani Plum", 0xFF6B2346),
                ProductColorOption("Antique Gold", 0xFFB68A46),
                ProductColorOption("Crimson Rose", 0xFF912E3B),
              ),
            defaultStock = 3,
            overrides = mapOf(ProductVariantKey(Product.ONE_SIZE, "Antique Gold") to 5),
          ),
        careInstructions =
          "Illustrative care: dry clean recommended for prototype festive finishes; store folded in cotton cloth.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration = "Design inspiration: Uttar Pradesh (Banaras-inspired jaal motifs)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Banaras-Inspired Festive Dupatta with gold lattice and paisley borders.",
        motif = TextileMotif.BANARAS_BROCADE,
        isFeatured = true,
      ),
      Product(
        id = "lucknow-embroidered-kurta",
        name = "Lucknow-Inspired Embroidered Kurta",
        region = Region.UTTAR_PRADESH,
        category = ProductCategory.KURTAS,
        priceInr = 2800,
        shortDescription =
          "Delicate floral trellis kurta inspired by Lucknowi tonal embroidery and soft pastel palettes.",
        fullDescription =
          "Tailored with a clean A-line fall and subtle front placket detailing, this sample design explores airy floral motifs suited for warm-weather daytime wear and semi-formal occasions.",
        illustrativeMaterial =
          "Illustrative demo fabric: lightweight cotton cambric with tonal thread-work inspired embroidery.",
        availableSizes = standardKurtaSizes,
        availableColors =
          listOf(
            ProductColorOption("Chalk Ivory", 0xFFF7F2EA),
            ProductColorOption("Sage Mist", 0xFF8C9F8C),
            ProductColorOption("Rosewood", 0xFF9C5D63),
          ),
        variantStock =
          buildStockMap(
            sizes = standardKurtaSizes,
            colors =
              listOf(
                ProductColorOption("Chalk Ivory", 0xFFF7F2EA),
                ProductColorOption("Sage Mist", 0xFF8C9F8C),
                ProductColorOption("Rosewood", 0xFF9C5D63),
              ),
            defaultStock = 4,
            overrides = mapOf(ProductVariantKey("XL", "Rosewood") to 0),
          ),
        careInstructions =
          "Illustrative care: gentle hand wash in cold water; turn inside out before ironing.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration =
          "Design inspiration: Uttar Pradesh (Lucknow-inspired floral trellis)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Lucknow-Inspired Embroidered Kurta featuring delicate floral trellis patterns.",
        motif = TextileMotif.LUCKNOW_TRELLIS,
        isFeatured = false,
      ),
      Product(
        id = "phulkari-inspired-dupatta",
        name = "Phulkari-Inspired Dupatta",
        region = Region.PUNJAB,
        category = ProductCategory.DUPATTAS,
        priceInr = 2400,
        shortDescription =
          "Vibrant geometric diamond stitch-inspired dupatta designed to brighten neutral everyday outfits.",
        fullDescription =
          "A lively statement piece that pairs bold geometric chevrons and floral diamond clusters with a crisp bordered drape, crafted for festive wear and effortless gifting.",
        illustrativeMaterial =
          "Illustrative demo fabric: cotton-base weave with dense geometric thread-stitch inspired accents.",
        availableSizes = listOf(Product.ONE_SIZE),
        availableColors =
          listOf(
            ProductColorOption("Marigold Ochre", 0xFFD0892F),
            ProductColorOption("Deep Maroon", 0xFF6B2346),
            ProductColorOption("Rust Coral", 0xFFB84A39),
          ),
        variantStock =
          buildStockMap(
            sizes = listOf(Product.ONE_SIZE),
            colors =
              listOf(
                ProductColorOption("Marigold Ochre", 0xFFD0892F),
                ProductColorOption("Deep Maroon", 0xFF6B2346),
                ProductColorOption("Rust Coral", 0xFFB84A39),
              ),
            defaultStock = 5,
          ),
        careInstructions =
          "Illustrative care: spot clean or gentle dry clean to protect raised stitch-style textures.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration = "Design inspiration: Punjab (Phulkari-inspired geometric motifs)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Phulkari-Inspired Dupatta displaying vibrant diamond stitch geometry.",
        motif = TextileMotif.PHULKARI_DIAMOND,
        isFeatured = false,
      ),
      Product(
        id = "aipan-printed-stole",
        name = "Aipan-Inspired Printed Stole",
        region = Region.UTTARAKHAND,
        category = ProductCategory.STOLES,
        priceInr = 1600,
        shortDescription =
          "Compact, easy-to-layer stole featuring crisp ivory-on-terracotta motifs inspired by Aipan patterns.",
        fullDescription =
          "Designed for everyday college layering and thoughtful gifting, this sample stole translates concentric borders and dotted geometric patterns into a wearable modern accessory.",
        illustrativeMaterial =
          "Illustrative demo fabric: soft brushed cotton-modal blend with edge-printed border motifs.",
        availableSizes = listOf(Product.ONE_SIZE),
        availableColors =
          listOf(
            ProductColorOption("Terracotta Red", 0xFF8F392E),
            ProductColorOption("Chalk Ivory", 0xFFF2EAE0),
          ),
        variantStock =
          buildStockMap(
            sizes = listOf(Product.ONE_SIZE),
            colors =
              listOf(
                ProductColorOption("Terracotta Red", 0xFF8F392E),
                ProductColorOption("Chalk Ivory", 0xFFF2EAE0),
              ),
            defaultStock = 6,
          ),
        careInstructions =
          "Illustrative care: cold hand wash with mild detergent; line dry away from direct sun.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration = "Design inspiration: Uttarakhand (Aipan-inspired border geometry)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Aipan-Inspired Printed Stole with concentric terracotta and ivory motifs.",
        motif = TextileMotif.AIPAN_GEOMETRY,
        isFeatured = false,
      ),
      Product(
        id = "pochampally-patterned-kurta",
        name = "Pochampally-Inspired Patterned Kurta",
        region = Region.TELANGANA,
        category = ProductCategory.KURTAS,
        priceInr = 2700,
        shortDescription =
          "Structured contemporary kurta with stepped diamond motifs inspired by Pochampally ikat geometry.",
        fullDescription =
          "Blending structured tailoring with rhythmic stepped diamond patterns, this sample piece bridges workwear minimalism and regional textile storytelling.",
        illustrativeMaterial =
          "Illustrative demo fabric: medium-weight cotton weave with ikat-inspired geometric print/pattern.",
        availableSizes = standardKurtaSizes,
        availableColors =
          listOf(
            ProductColorOption("Plum & Ivory", 0xFF6B2346),
            ProductColorOption("Charcoal & Gold", 0xFF3A3431),
          ),
        variantStock =
          buildStockMap(
            sizes = standardKurtaSizes,
            colors =
              listOf(
                ProductColorOption("Plum & Ivory", 0xFF6B2346),
                ProductColorOption("Charcoal & Gold", 0xFF3A3431),
              ),
            defaultStock = 3,
            overrides = mapOf(ProductVariantKey("S", "Plum & Ivory") to 5),
          ),
        careInstructions =
          "Illustrative care: cold gentle wash separately; dry in shade; medium iron.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration =
          "Design inspiration: Telangana (Pochampally-inspired stepped diamonds)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Pochampally-Inspired Patterned Kurta with stepped diamond ikat geometry.",
        motif = TextileMotif.POCHAMPALLY_IKAT,
        isFeatured = false,
      ),
      Product(
        id = "regional-fusion-kurta-set",
        name = "Regional Fusion Kurta Set",
        region = Region.BIHAR,
        category = ProductCategory.SETS,
        priceInr = 3000,
        shortDescription =
          "Coordinated two-piece kurta and trouser set paired with subtle Bihar-inspired heritage borders.",
        fullDescription =
          "Our signature prototype ensemble combining a tailored kurta and relaxed straight trousers with restrained gold-and-maroon border detailing—built for festive events, college farewells, and contemporary occasions.",
        illustrativeMaterial =
          "Illustrative demo fabric: breathable cotton-rayon blend set with contrast woven-style border trim.",
        availableSizes = fusionSetSizes,
        availableColors =
          listOf(
            ProductColorOption("Deep Plum", 0xFF6B2346),
            ProductColorOption("Warm Sand", 0xFFD9C7B0),
          ),
        variantStock =
          buildStockMap(
            sizes = fusionSetSizes,
            colors =
              listOf(
                ProductColorOption("Deep Plum", 0xFF6B2346),
                ProductColorOption("Warm Sand", 0xFFD9C7B0),
              ),
            defaultStock = 3,
            overrides = mapOf(ProductVariantKey("M", "Deep Plum") to 4),
          ),
        careInstructions =
          "Illustrative care: gentle cold wash or dry clean; steam press on low heat.",
        deliveryNote = DELIVERY_COPY,
        returnsNote = RETURNS_COPY,
        originStatus = "Supplier and production origin: pending verification",
        designInspiration =
          "Design inspiration: Bihar (contemporary fusion silhouette with regional border cues)",
        specificationNote = SPECIFICATION_NOTE,
        altText =
          "Textile illustration for Regional Fusion Kurta Set showing coordinated maroon and gold border accents.",
        motif = TextileMotif.REGIONAL_FUSION,
        isFeatured = true,
      ),
    )

  val stories: List<RegionalStory> =
    listOf(
      RegionalStory(
        region = Region.BIHAR,
        title = "Bihar: Mithila Lines & Bhagalpur Textures",
        craftFocus = "Initial beachhead market — Everyday kurtas, woven-inspired drapes & fusion sets",
        summary =
          "Riwaayat begins in Bihar by exploring two distinct visual languages: expressive double-line border motifs inspired by Mithila folk art and calm, earthy woven textures associated with Bhagalpur drapes.",
        prototypeNote =
          "This sample collection explores patterns and visual references associated with Bihar. Actual production methods and supplier origins would be verified before launch.",
        designHighlights =
          listOf(
            "Double-line lotus and geometric borders styled for clean everyday silhouettes.",
            "Earthy sandstone and deep plum tones suited for college and festive wardrobes.",
            "Planned direct discovery conversations with local suppliers during pilot validation.",
          ),
        motif = TextileMotif.MITHILA_LOTUS,
      ),
      RegionalStory(
        region = Region.UTTAR_PRADESH,
        title = "Uttar Pradesh: Banaras Radiance & Lucknowi Grace",
        craftFocus = "Planned expansion — Festive jaal drapes & soft floral trellises",
        summary =
          "From structured metallic-inspired lattice borders for evening celebrations to airy tonal floral motifs for daytime wear, our Uttar Pradesh sample pieces celebrate two contrasting moods of dressing.",
        prototypeNote =
          "This sample collection explores patterns and visual references associated with Uttar Pradesh. Actual production methods and supplier origins would be verified before launch.",
        designHighlights =
          listOf(
            "Muted-gold jaal lattice concepts tailored for weddings, fests, and gifting.",
            "Soft ivory and sage pastel palettes with delicate floral trellis accents.",
            "Designed to pair effortlessly across modern ethnic and fusion wardrobes.",
          ),
        motif = TextileMotif.BANARAS_BROCADE,
      ),
      RegionalStory(
        region = Region.PUNJAB,
        title = "Punjab: Phulkari-Inspired Color & Stitch Rhythm",
        craftFocus = "Planned expansion — Expressive geometric statement dupattas",
        summary =
          "Known for joyful color contrasts and radiating diamond geometry, our Punjab-inspired sample dupatta brings warmth and movement to minimalist kurtas without overwhelming everyday styling.",
        prototypeNote =
          "This sample collection explores patterns and visual references associated with Punjab. Actual production methods and supplier origins would be verified before launch.",
        designHighlights =
          listOf(
            "Warm marigold, rust coral, and deep maroon diamond stitch motifs.",
            "Balanced border framing so the piece wears comfortably from campus events to family functions.",
            "Accessible gift-ready One Size silhouette.",
          ),
        motif = TextileMotif.PHULKARI_DIAMOND,
      ),
      RegionalStory(
        region = Region.UTTARAKHAND,
        title = "Uttarakhand: Aipan Sacred Geometry in Modern Wear",
        craftFocus = "Planned expansion — Lightweight printed stoles for daily layering",
        summary =
          "Inspired by crisp ivory linework and dotted rhythmic borders set against warm terracotta red, our Uttarakhand sample stole translates ceremonial floor-art geometry into a light daily accessory.",
        prototypeNote =
          "This sample collection explores patterns and visual references associated with Uttarakhand. Actual production methods and supplier origins would be verified before launch.",
        designHighlights =
          listOf(
            "Concentric dot-and-border motifs adapted for clean edge framing.",
            "Compact stole proportions suited for hostel-to-workplace layering.",
            "Accessible entry price point for first-time regional fashion buyers.",
          ),
        motif = TextileMotif.AIPAN_GEOMETRY,
      ),
      RegionalStory(
        region = Region.TELANGANA,
        title = "Telangana: Pochampally-Inspired Stepped Symmetry",
        craftFocus = "Planned expansion — Structured geometric kurtas for work & college",
        summary =
          "Our Telangana-inspired sample kurta draws on the crisp stepped diamonds and feathered edges characteristic of ikat-inspired geometry, reimagined in calm plum and charcoal tones.",
        prototypeNote =
          "This sample collection explores patterns and visual references associated with Telangana. Actual production methods and supplier origins would be verified before launch.",
        designHighlights =
          listOf(
            "Architectural stepped-diamond patterns suited for smart-casual and workplace dressing.",
            "Restrained plum-and-ivory and charcoal-and-gold colorways.",
            "Detailed care and sizing guidance for confident online discovery.",
          ),
        motif = TextileMotif.POCHAMPALLY_IKAT,
      ),
    )

  fun findProductById(id: String): Product? = products.firstOrNull { it.id == id }

  fun productsForRegion(region: Region): List<Product> =
    products.filter { it.region == region }

  fun relatedProducts(product: Product, limit: Int = 3): List<Product> {
    val sameRegion = products.filter { it.id != product.id && it.region == product.region }
    val others = products.filter { it.id != product.id && it.region != product.region }
    return (sameRegion + others).take(limit)
  }

  fun createSampleEnquiries(nowMillis: Long = System.currentTimeMillis()): List<DemoEnquiryRecord> {
    val oneHour = 3_600_000L
    val sample1Items =
      listOf(
        EnquiryItemSnapshot(
          productId = "mithila-cotton-kurta",
          productName = "Mithila-Inspired Cotton Kurta",
          regionName = "Bihar",
          size = "M",
          color = "Warm Ivory",
          unitPriceInr = 2200,
          quantity = 1,
          lineTotalInr = 2200,
        ),
        EnquiryItemSnapshot(
          productId = "bhagalpur-woven-dupatta",
          productName = "Bhagalpur-Inspired Woven Dupatta",
          regionName = "Bihar",
          size = Product.ONE_SIZE,
          color = "Sandstone Gold",
          unitPriceInr = 1800,
          quantity = 1,
          lineTotalInr = 1800,
        ),
      )
    val sample2Items =
      listOf(
        EnquiryItemSnapshot(
          productId = "aipan-printed-stole",
          productName = "Aipan-Inspired Printed Stole",
          regionName = "Uttarakhand",
          size = Product.ONE_SIZE,
          color = "Terracotta Red",
          unitPriceInr = 1600,
          quantity = 2,
          lineTotalInr = 3200,
        )
      )

    return listOf(
      DemoEnquiryRecord(
        reference = "SAMPLE-RWY-101",
        createdAtDisplay = Formatters.formatTimestamp(nowMillis - 6 * oneHour),
        createdAtEpochMillis = nowMillis - 6 * oneHour,
        items = sample1Items,
        totalUnits = sample1Items.sumOf { it.quantity },
        estimatedMerchandiseTotalInr = sample1Items.sumOf { it.lineTotalInr },
        status = "Sample data — demonstration only",
        isSample = true,
      ),
      DemoEnquiryRecord(
        reference = "SAMPLE-RWY-102",
        createdAtDisplay = Formatters.formatTimestamp(nowMillis - 24 * oneHour),
        createdAtEpochMillis = nowMillis - 24 * oneHour,
        items = sample2Items,
        totalUnits = sample2Items.sumOf { it.quantity },
        estimatedMerchandiseTotalInr = sample2Items.sumOf { it.lineTotalInr },
        status = "Sample data — demonstration only",
        isSample = true,
      ),
    )
  }
}
