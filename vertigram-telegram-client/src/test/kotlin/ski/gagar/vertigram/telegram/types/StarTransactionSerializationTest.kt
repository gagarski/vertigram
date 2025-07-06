package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import java.time.Instant
import java.time.temporal.ChronoUnit

object StarTransactionSerializationTest : BaseSerializationTest() {
    @Test
    fun `transaction partner should survive serialization`() {
        assertSerializable<StarTransaction.TransactionPartner>(
            StarTransaction.TransactionPartner.User.create(
                transactionType = StarTransaction.TransactionPartner.User.TransactionType.INVOICE_PAYMENT,
                user = User.create(id = 1)
            )
        )

        assertSerializable<StarTransaction.TransactionPartner>(
            StarTransaction.TransactionPartner.Chat.create(
                chat = Chat.create(id = 1, type = Chat.Type.PRIVATE)
            )
        )

        assertSerializable<StarTransaction.TransactionPartner>(
            StarTransaction.TransactionPartner.AffiliateProgram.create(
                commissionPerMile = 100,
            )
        )

        assertSerializable<StarTransaction.TransactionPartner>(
            StarTransaction.TransactionPartner.Fragment.create(
                commissionPerMile = 100,
            )
        )

        assertSerializable<StarTransaction.TransactionPartner>(
            StarTransaction.TransactionPartner.TelegramAds
        )

        assertSerializable<StarTransaction.TransactionPartner>(
            StarTransaction.TransactionPartner.TelegramApi.create(
                requestCount = 1
            )
        )

        assertSerializable<StarTransaction.TransactionPartner>(
            StarTransaction.TransactionPartner.Other
        )
    }

    @Test
    fun `revenue withdrawal state should survive serialization`() {
        assertSerializable<StarTransaction.TransactionPartner.Fragment.RevenueWithdrawalState>(
            StarTransaction.TransactionPartner.Fragment.RevenueWithdrawalState.Pending
        )
        assertSerializable<StarTransaction.TransactionPartner.Fragment.RevenueWithdrawalState>(
            StarTransaction.TransactionPartner.Fragment.RevenueWithdrawalState.Succeeded.create(
                date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                url = "https://google.com"
            )
        )
        assertSerializable<StarTransaction.TransactionPartner.Fragment.RevenueWithdrawalState>(
            StarTransaction.TransactionPartner.Fragment.RevenueWithdrawalState.Failed
        )
    }
}