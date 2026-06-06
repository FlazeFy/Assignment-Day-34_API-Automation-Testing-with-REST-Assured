package body.transaction;

import org.json.simple.JSONObject;
import utils.JSONHelper;

public class TransactionBody {
    public JSONObject createTransactionValidData() {
        JSONObject body = new JSONObject();

        // Take activity_id & payment_id
        String activityId = JSONHelper.getJSONValueByKey("id", "sport-activity.json");
        String paymentMethodId = JSONHelper.getJSONValueByKey("id", "payment-method.json");

        body.put("sport_activity_id", Integer.parseInt(activityId));
        body.put("payment_method_id", Integer.parseInt(paymentMethodId));

        return body;
    }
}