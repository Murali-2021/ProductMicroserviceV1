package com.appsdeveloper.ws.ProductMicroservice.service;

import com.appsdeveloper.ws.ProductMicroservice.model.CreateProductRestModel;
//import jakarta.websocket.SendResult;
import com.appsdeveloperblog.ws.core.ProductCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceimpl implements ProductService{

    KafkaTemplate<String, ProductCreateEvent> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public ProductServiceimpl(KafkaTemplate<String, ProductCreateEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductCreateEvent productCreateEvent = new ProductCreateEvent(productId, productRestModel.getTitle(), productRestModel.getPrice(), productRestModel.getQuantity());

        //Method 1 for Synchornization
       /*
        CompletableFuture<SendResult<String, ProductCreateEvent>> future = kafkaTemplate.send("product-created-events-topic", productId, productCreateEvent);


        future.whenComplete((result, exception)->{
           if(exception != null)
               logger.info("Failed to Send Message..."+exception.getMessage());
           else
               logger.info("Message sent Successfully..."+result.getRecordMetadata());

           });

        future.join();//blocks the thread till confirmation is received
        */

        //Method 2 for Synchronization
        SendResult<String, ProductCreateEvent> result = kafkaTemplate.send("product-created-events-topic", productId, productCreateEvent).get();

        logger.info("Topic : "+result.getRecordMetadata().topic());
        logger.info("Partition : "+result.getRecordMetadata().partition());
        logger.info("Offset : "+result.getRecordMetadata().offset());

        logger.info("### Returning product id");
        //Method 1 for synchronization ends here



        return productId;
    }
}
