package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Region
import com.example.model.TextileMotif
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.DeepMaroon
import com.example.ui.theme.DeepMaroonDark
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedSurface
import com.example.ui.theme.SoftBorder
import com.example.ui.theme.WarmIvory
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ProductArtwork(
  productName: String,
  region: Region,
  motif: TextileMotif,
  altText: String,
  accentHex: Long? = null,
  modifier: Modifier = Modifier,
  showProductTitleOverlay: Boolean = true,
  isFallbackState: Boolean = false,
) {
  val palette = motifPalette(motif, accentHex)

  Box(
    modifier =
      modifier
        .clip(RoundedCornerShape(16.dp))
        .background(
          Brush.linearGradient(
            colors = listOf(palette.backgroundTop, palette.backgroundBottom)
          )
        )
        .border(1.dp, SoftBorder, RoundedCornerShape(16.dp))
        .semantics(mergeDescendants = true) { contentDescription = altText }
  ) {
    if (!isFallbackState) {
      Canvas(modifier = Modifier.fillMaxSize().clearAndSetSemantics {}) {
        val w = size.width
        val h = size.height
        val pad = minOf(w, h) * 0.08f

        // Subtle woven warp & weft lines
        val step = (minOf(w, h) / 14f).coerceAtLeast(10f)
        var x = 0f
        while (x < w) {
          drawLine(
            color = palette.lineTint.copy(alpha = 0.08f),
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 1f,
          )
          x += step
        }
        var y = 0f
        while (y < h) {
          drawLine(
            color = palette.lineTint.copy(alpha = 0.08f),
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 1f,
          )
          y += step
        }

        // Outer heritage frame
        drawRect(
          color = palette.goldAccent.copy(alpha = 0.55f),
          topLeft = Offset(pad, pad),
          size = Size(w - pad * 2, h - pad * 2),
          style = Stroke(width = 2f),
        )
        drawRect(
          color = palette.primaryStroke.copy(alpha = 0.35f),
          topLeft = Offset(pad * 1.45f, pad * 1.45f),
          size = Size(w - pad * 2.9f, h - pad * 2.9f),
          style = Stroke(width = 1.2f),
        )

        val cx = w / 2f
        val cy = h * 0.44f
        val radius = minOf(w, h) * 0.24f

        when (motif) {
          TextileMotif.MITHILA_LOTUS -> {
            // Double-ring lotus mandala + scalloped border dots
            drawCircle(
              color = palette.primaryStroke.copy(alpha = 0.7f),
              radius = radius,
              center = Offset(cx, cy),
              style = Stroke(width = 3f),
            )
            drawCircle(
              color = palette.goldAccent.copy(alpha = 0.8f),
              radius = radius * 0.72f,
              center = Offset(cx, cy),
              style = Stroke(width = 2f),
            )
            for (i in 0 until 8) {
              val angle = (i * Math.PI / 4.0).toFloat()
              val px = cx + cos(angle) * radius * 0.85f
              val py = cy + sin(angle) * radius * 0.85f
              drawCircle(
                color = palette.primaryStroke.copy(alpha = 0.55f),
                radius = radius * 0.22f,
                center = Offset(px, py),
                style = Stroke(width = 2f),
              )
            }
            drawCircle(
              color = palette.goldAccent,
              radius = radius * 0.22f,
              center = Offset(cx, cy),
            )
          }
          TextileMotif.BHAGALPUR_WEAVE -> {
            // Textured horizontal weave bands & temple triangles
            for (row in 0 until 5) {
              val bandY = h * (0.24f + row * 0.1f)
              drawLine(
                color =
                  if (row % 2 == 0) palette.primaryStroke.copy(alpha = 0.65f)
                  else palette.goldAccent.copy(alpha = 0.85f),
                start = Offset(pad * 2f, bandY),
                end = Offset(w - pad * 2f, bandY),
                strokeWidth = if (row == 2) 5f else 2.5f,
              )
            }
            val templeCount = 6
            val span = (w - pad * 4f) / templeCount
            for (t in 0 until templeCount) {
              val tx = pad * 2f + t * span
              val path =
                Path().apply {
                  moveTo(tx, h * 0.62f)
                  lineTo(tx + span / 2f, h * 0.53f)
                  lineTo(tx + span, h * 0.62f)
                  close()
                }
              drawPath(path, color = palette.primaryStroke.copy(alpha = 0.45f))
            }
          }
          TextileMotif.BANARAS_BROCADE -> {
            // Gold jaal diamond lattice
            val cols = 4
            val cellW = (w - pad * 3.5f) / cols
            val startX = pad * 1.75f
            val startY = h * 0.22f
            for (r in 0 until 3) {
              for (c in 0 until cols) {
                val dx = startX + c * cellW + cellW / 2f
                val dy = startY + r * (cellW * 0.55f)
                val diamond =
                  Path().apply {
                    moveTo(dx, dy - cellW * 0.26f)
                    lineTo(dx + cellW * 0.42f, dy)
                    lineTo(dx, dy + cellW * 0.26f)
                    lineTo(dx - cellW * 0.42f, dy)
                    close()
                  }
                drawPath(
                  diamond,
                  color = palette.goldAccent.copy(alpha = 0.85f),
                  style = Stroke(width = 2.2f),
                )
                drawCircle(
                  color = palette.primaryStroke.copy(alpha = 0.65f),
                  radius = 3.5f,
                  center = Offset(dx, dy),
                )
              }
            }
          }
          TextileMotif.LUCKNOW_TRELLIS -> {
            // Soft floral chikankari-inspired trellis
            for (i in 0 until 6) {
              val angle = (i * Math.PI / 3.0).toFloat()
              val px = cx + cos(angle) * radius * 0.58f
              val py = cy + sin(angle) * radius * 0.58f
              drawCircle(
                color = palette.primaryStroke.copy(alpha = 0.45f),
                radius = radius * 0.34f,
                center = Offset(px, py),
                style = Stroke(width = 2f),
              )
            }
            drawCircle(
              color = palette.goldAccent.copy(alpha = 0.7f),
              radius = radius * 0.24f,
              center = Offset(cx, cy),
              style = Stroke(width = 2.5f),
            )
          }
          TextileMotif.PHULKARI_DIAMOND -> {
            // Concentric geometric Phulkari stitch diamonds
            listOf(1.1f, 0.78f, 0.46f).forEachIndexed { idx, scale ->
              val d = radius * scale
              val path =
                Path().apply {
                  moveTo(cx, cy - d)
                  lineTo(cx + d, cy)
                  lineTo(cx, cy + d)
                  lineTo(cx - d, cy)
                  close()
                }
              drawPath(
                path,
                color =
                  if (idx % 2 == 0) palette.primaryStroke.copy(alpha = 0.75f)
                  else palette.goldAccent.copy(alpha = 0.9f),
                style = Stroke(width = 3.2f),
              )
            }
            drawCircle(
              color = palette.primaryStroke.copy(alpha = 0.8f),
              radius = radius * 0.16f,
              center = Offset(cx, cy),
            )
          }
          TextileMotif.AIPAN_GEOMETRY -> {
            // Aipan concentric rings & sacred dot ring
            drawCircle(
              color = palette.primaryStroke.copy(alpha = 0.85f),
              radius = radius,
              center = Offset(cx, cy),
              style = Stroke(width = 3f),
            )
            drawCircle(
              color = palette.primaryStroke.copy(alpha = 0.65f),
              radius = radius * 0.68f,
              center = Offset(cx, cy),
              style = Stroke(width = 2f),
            )
            for (i in 0 until 12) {
              val angle = (i * Math.PI / 6.0).toFloat()
              drawCircle(
                color = palette.goldAccent,
                radius = 4f,
                center = Offset(cx + cos(angle) * radius * 0.84f, cy + sin(angle) * radius * 0.84f),
              )
            }
            val innerStar =
              Path().apply {
                moveTo(cx, cy - radius * 0.48f)
                lineTo(cx + radius * 0.42f, cy + radius * 0.28f)
                lineTo(cx - radius * 0.42f, cy + radius * 0.28f)
                close()
              }
            drawPath(innerStar, color = palette.primaryStroke.copy(alpha = 0.6f), style = Stroke(2.2f))
          }
          TextileMotif.POCHAMPALLY_IKAT -> {
            // Stepped ikat diamonds
            val steps = 3
            for (s in 0 until steps) {
              val r = radius * (1.05f - s * 0.28f)
              val boxSize = r * 1.4f
              drawRect(
                color =
                  if (s % 2 == 0) palette.primaryStroke.copy(alpha = 0.7f)
                  else palette.goldAccent.copy(alpha = 0.85f),
                topLeft = Offset(cx - boxSize / 2f, cy - boxSize / 2f),
                size = Size(boxSize, boxSize),
                style = Stroke(width = 2.6f),
              )
            }
            drawLine(
              color = palette.primaryStroke.copy(alpha = 0.45f),
              start = Offset(pad * 2f, cy),
              end = Offset(w - pad * 2f, cy),
              strokeWidth = 2f,
            )
          }
          TextileMotif.REGIONAL_FUSION -> {
            // Twin heritage arch + gold center medallion
            val arch =
              Path().apply {
                moveTo(cx - radius, cy + radius * 0.7f)
                lineTo(cx - radius, cy - radius * 0.2f)
                quadraticTo(cx, cy - radius * 1.15f, cx + radius, cy - radius * 0.2f)
                lineTo(cx + radius, cy + radius * 0.7f)
                close()
              }
            drawPath(arch, color = palette.primaryStroke.copy(alpha = 0.72f), style = Stroke(3f))
            drawCircle(
              color = palette.goldAccent.copy(alpha = 0.85f),
              radius = radius * 0.45f,
              center = Offset(cx, cy),
              style = Stroke(width = 2.4f),
            )
            drawCircle(
              color = palette.primaryStroke.copy(alpha = 0.7f),
              radius = radius * 0.18f,
              center = Offset(cx, cy),
            )
          }
        }
      }
    }

    if (showProductTitleOverlay) {
      Column(
        modifier =
          Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
      ) {
        Surface(
          color = WarmIvory.copy(alpha = 0.92f),
          shape = RoundedCornerShape(50),
          border = BorderStroke(1.dp, SoftBorder),
        ) {
          Text(
            text = if (isFallbackState) "Textile Placeholder" else region.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = DeepMaroonDark,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          )
        }

        Surface(
          color = WarmIvory.copy(alpha = 0.93f),
          shape = RoundedCornerShape(10.dp),
          border = BorderStroke(1.dp, SoftBorder),
          modifier = Modifier.fillMaxWidth(),
        ) {
          Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)) {
            Text(
              text = productName,
              style = MaterialTheme.typography.labelLarge,
              color = CharcoalText,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
              textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Prototype textile illustration",
              style = MaterialTheme.typography.labelSmall,
              color = MutedGold,
              fontWeight = FontWeight.Medium,
            )
          }
        }
      }
    }
  }
}

private data class MotifPalette(
  val backgroundTop: Color,
  val backgroundBottom: Color,
  val primaryStroke: Color,
  val goldAccent: Color,
  val lineTint: Color,
)

private fun motifPalette(motif: TextileMotif, accentHex: Long?): MotifPalette {
  val customAccent = accentHex?.let { Color(it) }
  return when (motif) {
    TextileMotif.MITHILA_LOTUS ->
      MotifPalette(
        backgroundTop = Color(0xFFF6EFE5),
        backgroundBottom = Color(0xFFEADCCF),
        primaryStroke = customAccent?.takeIf { it != Color(0xFFF3ECE0) } ?: DeepMaroon,
        goldAccent = MutedGold,
        lineTint = DeepMaroon,
      )
    TextileMotif.BHAGALPUR_WEAVE ->
      MotifPalette(
        backgroundTop = Color(0xFFF4ECDF),
        backgroundBottom = Color(0xFFE6D7C3),
        primaryStroke = customAccent ?: Color(0xFF7D4E2D),
        goldAccent = MutedGold,
        lineTint = Color(0xFF7D4E2D),
      )
    TextileMotif.BANARAS_BROCADE ->
      MotifPalette(
        backgroundTop = Color(0xFF5D1D3C),
        backgroundBottom = Color(0xFF3E1227),
        primaryStroke = WarmIvory,
        goldAccent = Color(0xFFD6AE68),
        lineTint = WarmIvory,
      )
    TextileMotif.LUCKNOW_TRELLIS ->
      MotifPalette(
        backgroundTop = Color(0xFFF7F4EE),
        backgroundBottom = Color(0xFFE8EFE7),
        primaryStroke = customAccent?.takeIf { it != Color(0xFFF7F2EA) } ?: Color(0xFF6B7F6E),
        goldAccent = MutedGold,
        lineTint = Color(0xFF6B7F6E),
      )
    TextileMotif.PHULKARI_DIAMOND ->
      MotifPalette(
        backgroundTop = Color(0xFFFCF0DF),
        backgroundBottom = Color(0xFFF2DFC4),
        primaryStroke = customAccent ?: Color(0xFF9C3428),
        goldAccent = Color(0xFFD0892F),
        lineTint = DeepMaroon,
      )
    TextileMotif.AIPAN_GEOMETRY ->
      MotifPalette(
        backgroundTop = Color(0xFF8C362B),
        backgroundBottom = Color(0xFF6A231B),
        primaryStroke = WarmIvory,
        goldAccent = Color(0xFFE8C68E),
        lineTint = WarmIvory,
      )
    TextileMotif.POCHAMPALLY_IKAT ->
      MotifPalette(
        backgroundTop = MutedSurface,
        backgroundBottom = Color(0xFFE2D8CE),
        primaryStroke = customAccent ?: DeepMaroon,
        goldAccent = MutedGold,
        lineTint = CharcoalText,
      )
    TextileMotif.REGIONAL_FUSION ->
      MotifPalette(
        backgroundTop = Color(0xFFF3E9ED),
        backgroundBottom = Color(0xFFE5D4DC),
        primaryStroke = customAccent ?: DeepMaroon,
        goldAccent = MutedGold,
        lineTint = DeepMaroon,
      )
  }
}
