const crypto = require('crypto');
const ESEWA_SECRET_KEY = "8g8ba5g6a1h";
const PRODUCT_CODE = "EPAYTEST";

function initiateNepalPayment(playerUuid, amount) {
    const transactionUuid = `PKYN-${playerUuid}-${Date.now()}`;
    const dataToSign = `total_amount=${amount},transaction_uuid=${transactionUuid},product_code=${PRODUCT_CODE}`;
    
    const hashSignature = crypto
        .createHmac('sha256', ESEWA_SECRET_KEY)
        .update(dataToSign)
        .digest('base64');

    return {
        amount: amount,
        transaction_uuid: transactionUuid,
        product_code: PRODUCT_CODE,
        signature: hashSignature,
        routing: "70% Corporate Account / 30% Capital Account Enabled"
    };
}
console.log("[BANK GATEWAY READY] Secure Multi-Bank split matrix initialized.");
