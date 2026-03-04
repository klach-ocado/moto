package moto.tests;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.support.MessageBuilder;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;

class SpringCloudAwsSqsTest {

    private static final String QUEUE_NAME = "test-queue";

    private SqsAsyncClient sqsAsyncClient = DependencyFactory.sqsAsyncClient();
    private String queueUrl;

    @BeforeEach
    void setUp() {
        queueUrl = sqsAsyncClient.createQueue(CreateQueueRequest.builder().queueName(QUEUE_NAME).build()).join().queueUrl();
    }

    @AfterEach
    void tearDown() {
        if (queueUrl != null) {
            sqsAsyncClient.deleteQueue(DeleteQueueRequest.builder().queueUrl(queueUrl).build()).join();
        }
    }

    @Test
    void testSendWithSqsTemplate() {
        SqsTemplate sqsTemplate = SqsTemplate.newTemplate(sqsAsyncClient);
        MyPojo myPojo = new MyPojo();
        myPojo.setMessage("Hello World");
        sqsTemplate.send(QUEUE_NAME, MessageBuilder.withPayload(myPojo).setHeader("numerical-attribute", 42).build());
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
