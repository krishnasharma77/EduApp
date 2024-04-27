package com.adts.app.model

data class PurchaseCourseModel(
    val `data`: Data,
    val message: String,
    val status: String
) {
    data class Data(
        val Amount: String,
        val CourseId: Int,
        val CreatedDate: String,
        val Id: Int,
        val LocalOrderId: String,
        val PaymentId: String,
        val PaymentStatus: String,
        val RazorpayPaymentId: String,
        val Reason: String,
        val UserId: Int
    )
}