### Require:

postman, jdk 8 and IntelliJ (It is better to use the services tab in IntelliJ which is in View-> Tool Windows -> Services).

### Frontend project:

Gloria's repository:https://github.com/gloriafer18/obs-rules/tree/gloriaRules

### Service running order:

1). eureka Server
   2). video, biodata(empatica), position services

### The API list:

   1. eureka server: http://localhost:8761/eureka/
      
   
   2. biodata service: http://localhost:7301			
      start recording: http://localhost:7301/bio/start/{sessionid}			
      stop recording: http://localhost:7301/bio/stop			
      device id:			
      0x6a18	27160			
      0x6a5a	27226			
      0x6a7d	27261			
      0x6a7e 	27262		
      0x6a7f	27263
   
   
   3. video service: http://localhost:7201			
      start recording: http://localhost:7101/video/start/{sessionid}			
      stop recording: http://localhost:7101/video/stop
   

   4. position service: http://localhost:7101			
      start recording: http://localhost:7201/pos/start/{sessionid}			
      stop recording: http://localhost:7201/pos/stop			
   
      device id: 	
      
      379efd (9557)			
      3b9efd (10668)			
      a39bfd (10742)


   5. audio service: http://localhost:7501			
      start : http://localhost:7501/audio/start/{seesionid}			
      stop: http://localhost:7501/audio/stop			


   6. each modality has one topic

      position topic			
      empatica topic			

      kafka data format: {"key":"key-sessionid-uuid", "value":"message"}
      position data will be saved to one sessionid.json file
