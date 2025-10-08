import com.pennywiseai.parser.core.TransactionType
import com.pennywiseai.parser.core.bank.JuspayParser
import com.pennywiseai.parser.core.test.ExpectedTransaction
import com.pennywiseai.parser.core.test.ParserTestCase
import com.pennywiseai.parser.core.test.ParserTestUtils
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class JuspayParserTest {

    @Test
    fun `juspay parser handles apay wallet transactions`() {
        val parser = JuspayParser()

        ParserTestUtils.printTestHeader(
            parserName = "Juspay (Amazon Pay)",
            bankName = parser.getBankName(),
            currency = parser.getCurrency()
        )

        val testCases = listOf(
            ParserTestCase(
                name = "Apay wallet debit transaction",
                message = "Your Apay Wallet balance is debited for INR 250.00 Transaction Reference Number is 123456789012 - Powered by Juspay",
                sender = "JUSPAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("250.00"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    reference = "123456789012",
                    merchant = "Amazon Pay Transaction"
                )
            ),
            ParserTestCase(
                name = "Payment using Apay balance",
                message = "Payment of Rs 150.50 using Apay Balance successful at merchant. Updated Balance is Rs 850.00 - SMS by Juspay",
                sender = "APAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("150.50"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    merchant = "merchant"
                )
            ),
            ParserTestCase(
                name = "Amazon transaction via Juspay",
                message = "Payment of Rs 1,250.00 using Apay Balance successful at Amazon. Updated Balance is Rs 2,500.00 - SMS by Juspay",
                sender = "JUSPAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("1250.00"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    merchant = "Amazon"
                )
            ),
            ParserTestCase(
                name = "Swiggy transaction via Apay",
                message = "Your Apay Wallet balance is debited for INR 450.75 Transaction Reference Number is 987654321098 - Powered by Juspay",
                sender = "JUSPAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("450.75"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    reference = "987654321098",
                    merchant = "Amazon Pay Transaction"
                )
            ),
            ParserTestCase(
                name = "Zepto delivery via Apay",
                message = "Payment of Rs 85.25 using Apay Balance successful at Zepto. Updated Balance is Rs 1,200.00 - SMS by Juspay",
                sender = "APAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("85.25"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    merchant = "Zepto"
                )
            ),
            ParserTestCase(
                name = "Flipkart transaction",
                message = "Payment of Rs 2,999.00 using Apay Balance successful at Flipkart. Updated Balance is Rs 5,000.00 - SMS by Juspay",
                sender = "JUSPAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("2999.00"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    merchant = "Flipkart"
                )
            ),
            ParserTestCase(
                name = "Small amount transaction",
                message = "Your Apay Wallet balance is debited for INR 10.00 Transaction Reference Number is 555555555555 - Powered by Juspay",
                sender = "JUSPAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("10.00"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    reference = "555555555555",
                    merchant = "Amazon Pay Transaction"
                )
            ),
            ParserTestCase(
                name = "Large amount transaction",
                message = "Payment of Rs 15,000.00 using Apay Balance successful at Amazon. Updated Balance is Rs 25,000.00 - SMS by Juspay",
                sender = "AMAZON PAY",
                expected = ExpectedTransaction(
                    amount = BigDecimal("15000.00"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    merchant = "Amazon"
                )
            )
        )

        val handleChecks = listOf(
            "JUSPAY" to true,
            "APAY" to true,
            "AMAZON PAY" to true,
            "XX-JUSPAY-X" to true,
            "JM-JUSPAY-A" to true,
            "UNKNOWN" to false,
            "HDFC" to false
        )

        val result = ParserTestUtils.runTestSuite(
            parser = parser,
            testCases = testCases,
            handleCases = handleChecks,
            suiteName = "Juspay (Amazon Pay) Parser"
        )

        ParserTestUtils.printTestSummary(
            totalTests = result.totalTests,
            passedTests = result.passedTests,
            failedTests = result.failedTests,
            failureDetails = result.failureDetails
        )
    }

    @Test
    fun `factory resolves juspay`() {
        val cases = listOf(
            com.pennywiseai.parser.core.test.SimpleTestCase(
                bankName = "Amazon Pay",
                sender = "JUSPAY",
                currency = "INR",
                message = "Your Apay Wallet balance is debited for INR 200.00 Transaction Reference Number is 123456789012 - Powered by Juspay",
                expected = ExpectedTransaction(
                    amount = BigDecimal("200.00"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    reference = "123456789012",
                    merchant = "Amazon Pay Transaction"
                ),
                shouldHandle = true
            ),
            com.pennywiseai.parser.core.test.SimpleTestCase(
                bankName = "Amazon Pay",
                sender = "APAY",
                currency = "INR",
                message = "Payment of Rs 350.00 using Apay Balance successful at Swiggy. Updated Balance is Rs 650.00 - SMS by Juspay",
                expected = ExpectedTransaction(
                    amount = BigDecimal("350.00"),
                    currency = "INR",
                    type = TransactionType.EXPENSE,
                    merchant = "Swiggy"
                ),
                shouldHandle = true
            )
        )

        val suite = ParserTestUtils.runFactoryTestSuite(cases, "Juspay Factory Tests")
        ParserTestUtils.printTestSummary(
            totalTests = suite.totalTests,
            passedTests = suite.passedTests,
            failedTests = suite.failedTests,
            failureDetails = suite.failureDetails
        )
    }
}