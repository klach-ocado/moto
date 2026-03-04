package moto.tests;

import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.DeleteTopicRequest;

import java.util.Map;

class SpringCloudAwsSnsTest {

    private static final String TOPIC_NAME = "test-topic";

    private SnsClient snsClient = DependencyFactory.snsClient();
    private String topicArn;

    @BeforeEach
    void setUp() {
        topicArn = snsClient.createTopic(CreateTopicRequest.builder().name(TOPIC_NAME).build()).topicArn();
    }

    @AfterEach
    void tearDown() {
        if (topicArn != null) {
            snsClient.deleteTopic(DeleteTopicRequest.builder().topicArn(topicArn).build());
        }
    }

    @Test
    void testSendWithSnsTemplate() {
        SnsTemplate snsTemplate = new SnsTemplate(snsClient);
        MyPojo myPojo = new MyPojo();
        myPojo.setMessage("Hello World");
        snsTemplate.convertAndSend(TOPIC_NAME, myPojo, Map.of("numerical-attribute", 42));
        // expect no exception thrown
    }

    static class MyPojo {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
