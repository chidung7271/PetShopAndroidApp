package app.code.petshopandroidapp.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.ibm.icu.text.RuleBasedNumberFormat
import com.ibm.icu.util.ULocale
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.io.image.ImageDataFactory
import app.code.petshopandroidapp.data.model.CartResponse
import java.net.URL
import app.code.petshopandroidapp.data.model.CustomerResponse
import app.code.petshopandroidapp.ui.model.CartItem
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BillGenerator(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun generateBill(
        customer: CustomerResponse,
        items: List<CartItem>,
        totalAmount: String
    ): String {
        // Validate parameters
        requireNotNull(customer.name) { "Customer name cannot be null" }
        requireNotNull(customer.phone) { "Customer phone cannot be null" }

        val currentTime = LocalDateTime.now()
        val fileName = "PetShop_Bill_${currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))}.pdf"
        
        // Save directly to Downloads folder
        val filePath = File(Environment.getExternalStorageDirectory().toString() + "/Download", fileName)

        // Create parent directories if they don't exist
        filePath.parentFile?.mkdirs()

        // Initialize PDF writer and document
        val writer = PdfWriter(filePath)
        val pdfDoc = PdfDocument(writer)
        val doc = Document(pdfDoc, PageSize.A5)

        try {
            // Use default font if custom font is not available
            val font = try {
                context.assets.open("fonts/NotoSans-Regular.ttf").use { inputStream ->
                    val fontBytes = inputStream.readBytes()
                    PdfFontFactory.createFont(
                        fontBytes,
                        "Identity-H",
                        PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED
                    )
                }
            } catch (e: Exception) {
                // Fallback to default font
                PdfFontFactory.createFont()
            }
            
            // Header
            val header = Paragraph("Pet Shop Management Application")
                .setFont(font)
                .setFontSize(16f)
                .setTextAlignment(TextAlignment.CENTER)
            doc.add(header)

            doc.add(
                Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setFont(font)
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
            )

            // Date
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            doc.add(
                Paragraph("Ngày: ${currentTime.format(dateFormatter)}")
                    .setFont(font)
                    .setFontSize(12f)
            )

            // Customer Info
            doc.add(
                Paragraph("Khách hàng: ${customer.name}")
                    .setFont(font)
                    .setFontSize(12f)
            )
            doc.add(
                Paragraph("SĐT: ${customer.phone}")
                    .setFont(font)
                    .setFontSize(12f)
            )

            // Items table
            val table = Table(UnitValue.createPercentArray(floatArrayOf(40f, 20f, 20f, 20f)))
                .setWidth(UnitValue.createPercentValue(100f))

            // Table header
            table.addHeaderCell(Cell().add(Paragraph("Tên").setFont(font)))
            table.addHeaderCell(Cell().add(Paragraph("Loại").setFont(font)))
            table.addHeaderCell(Cell().add(Paragraph("SL").setFont(font)))
            table.addHeaderCell(Cell().add(Paragraph("Giá").setFont(font)))

            // Table content
            items.forEach { item ->
                table.addCell(Cell().add(Paragraph(item.name).setFont(font)))
                table.addCell(Cell().add(Paragraph(if (item.type == "PRODUCT") "Sản phẩm" else "Dịch vụ").setFont(font)))
                table.addCell(Cell().add(Paragraph(item.quantity.toString()).setFont(font)))
                table.addCell(Cell().add(Paragraph("${item.price}₫").setFont(font)))
            }

            doc.add(table)

            // Total amount
            doc.add(
                Paragraph("Tổng tiền: $totalAmount₫")
                    .setFont(font)
                    .setFontSize(12f)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
            )

            // QR Code for VietQR payment
            try {
                Log.d("BillGenerator", "Bắt đầu tạo QR code...")
                
                // VietQR configuration - Thay đổi thông tin này theo tài khoản của bạn
                val bankId = "970436"
                val accountNo = "dungtapcode"
                val accountName = "DOAN CHI DUNG"
                val template = "compact2"
                
                Log.d("BillGenerator", "Thông tin tài khoản - Bank: $bankId, Account: $accountNo, Name: $accountName")
                
                val cleanAmount = totalAmount.replace(",", "").replace("₫", "").trim()
                val addInfo = "Thanh toan hoa don ${currentTime.format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"))}"
                
                Log.d("BillGenerator", "Số tiền: $cleanAmount, Nội dung: $addInfo")
                
                // Create VietQR URL
                val qrUrl = "https://img.vietqr.io/image/$bankId-$accountNo-$template.png?amount=$cleanAmount&addInfo=$addInfo&accountName=$accountName"
                
                Log.d("BillGenerator", "URL QR: $qrUrl")
                
                // Download and add QR code image on IO thread to avoid NetworkOnMainThreadException
                Log.d("BillGenerator", "Đang tải ảnh QR từ URL...")
                withContext(Dispatchers.IO) {
                    try {
                        val imageData = ImageDataFactory.create(URL(qrUrl))
                        Log.d("BillGenerator", "Tải ảnh QR thành công")
                        
                        val qrImage = Image(imageData)
                            .setWidth(400f)
                            .setHeight(400f)
                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        
                        Log.d("BillGenerator", "Ảnh QR được cấu hình: 150x150px")
                        
                        doc.add(Paragraph("\n")) // Add spacing
                        doc.add(
                            Paragraph("Quét mã QR để thanh toán:")
                                .setFont(font)
                                .setFontSize(10f)
                                .setTextAlignment(TextAlignment.CENTER)
                        )
                        doc.add(qrImage)
                        Log.d("BillGenerator", "Ảnh QR đã được thêm vào hóa đơn thành công")
                    } catch (e: Exception) {
                        Log.e("BillGenerator", "Lỗi khi tải ảnh QR: ${e.message}", e)
                    }
                }
                
            } catch (e: Exception) {
                // If QR code generation fails, continue without it
                Log.e("BillGenerator", "Lỗi khi tạo QR code: ${e.message}", e)
                Log.e("BillGenerator", "Stack trace: ${e.stackTraceToString()}")
                e.printStackTrace()
            }

            // Footer
            doc.add(
                Paragraph("\nCảm ơn Quý khách. Hẹn gặp lại!")
                    .setFont(font)
                    .setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic()
            )

        } finally {
            doc.close()
        }

        return filePath.absolutePath
    }
} 