package it.gov.pagopa.tkm.ms.parretriever.client.amex.tokenization;



import it.gov.pagopa.tkm.ms.parretriever.client.amex.enums.NotificationType;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.models.ApiClientRequest;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.utils.JsonUtility;

import java.util.HashMap;
import java.util.Map;

public class NotificationsRequest extends ApiClientRequest {

    private final String tokenReferenceId;
    private final NotificationType notificationType;
    private final String NOTIFICATIONS_TARGET_URI = "/payments/digital/v2/tokens/notifications";

    private NotificationsRequest(String tokenReferenceId, NotificationType notificationType) {
        this.tokenReferenceId = tokenReferenceId;
        this.notificationType = notificationType;
    }

    public static class NotificationsRequestBuilder {

        private String tokenReferenceId;
        private NotificationType notificationType;

        public NotificationsRequestBuilder setTokenReferenceId(String tokenReferenceId) {
            this.tokenReferenceId = tokenReferenceId;
            return this;
        }

        public NotificationsRequestBuilder setNotificationType(NotificationType notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public NotificationsRequest createNotificationsRequest() {
            return new NotificationsRequest(tokenReferenceId, notificationType);
        }
    }

    @Override
    public String toJson(String kid, String aesKey) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("token_ref_id", tokenReferenceId);
        map.put("notification_type", notificationType.getType());

        return JsonUtility.getInstance().getString(map);
    }

    @Override
    public String getUri() {
        return NOTIFICATIONS_TARGET_URI;
    }

    @Override
    public String getHttpAction() {
        return "POST";
    }
}