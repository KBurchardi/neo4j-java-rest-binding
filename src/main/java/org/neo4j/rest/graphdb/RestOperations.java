package org.neo4j.rest.graphdb;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.MediaType;

import org.neo4j.rest.graphdb.RestOperations.RestOperation.Methods;

public class RestOperations {
    private AtomicLong currentBatchId = new AtomicLong(0);
    private Map<Long, RestOperation> operations = new LinkedHashMap<Long, RestOperation>();
    private MediaType contentType;
    private MediaType acceptHeader; 
    
    public RestOperations(){
        this.contentType = MediaType.APPLICATION_JSON_TYPE;
        this.acceptHeader = MediaType.APPLICATION_JSON_TYPE;
    }
    
    public static class RestOperation { 
        public enum Methods{
            POST,
            PUT,
            GET,
            DELETE
        }
        
        private Methods method;
        private Object data;
        private final String baseUri;
        private long batchId;
        private String uri;
        private MediaType contentType;
        private MediaType acceptHeader;
        private Object entity;
        
       

        public RestOperation(long batchId, Methods method, String uri, MediaType contentType, MediaType acceptHeader, Object data, String baseUri){
            this.batchId = batchId;
            this.method = method;
            this.uri = uri;
            this.contentType = contentType;
            this.acceptHeader = acceptHeader;
            this.data = data;
            this.baseUri = baseUri;
        }
        
        public void updateEntity(Object updateObject, RestAPI restApi){
            if (this.entity instanceof UpdatableRestResult){
                ((UpdatableRestResult)this.entity).updateFrom(updateObject, restApi);
            }
        }
        
        public Object getEntity() {
            return entity;
        }

        public void setEntity(Object entity) {
            this.entity = entity;
        }
        
        public Methods getMethod() {
            return method;
        }

        public Object getData() {
            return data;
        }

        public long getBatchId() {
            return batchId;
        }

        public String getUri() {
            return uri;
        }

        public MediaType getContentType() {
            return contentType;
        }

        public MediaType getAcceptHeader() {
            return acceptHeader;
        }

        public String getBaseUri() {
            return baseUri;
        }
        public boolean isSameUri(String baseUri) {
            return this.baseUri.equals(baseUri);
        }
    }
    
    public Map<Long,RestOperation> getRecordedRequests(){
        return this.operations;
    }
    
    public RequestResult record(Methods method, String path, Object data, String baseUri){
        long batchId = this.currentBatchId.incrementAndGet();
        RestOperation r = new RestOperation(batchId,method,path,this.contentType,this.acceptHeader,data,baseUri);
        operations.put(batchId,r);
        return RequestResult.batchResult(r);
    }
    
    public void addToRestOperation(long batchId, Object entity){
        this.operations.get(batchId).setEntity(entity);
    }
}
