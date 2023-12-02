# Clean Architecture
### LAYERS
	Domain:
		This contains all the entities , aggregates , value-objects, business logics. 
		As per DDD we need to create rich models instead of plain old java objects (like we did in OOPs).
		It defines INTERFACES/PORTS for application and infra layers.
		INTERFACES/PORTS will then be consumed or implemented in the same layers.
		INTERFACES/PORTS for application layer are known PRIMARY PORTS and implementataions as PRIMARY ADAPTERS.
		INTERFACES/PORTS for infra layer are known as SECONDARY PORTS and implementations as SECONDARY ADAPTERS.
		Domain layer cannot have any dependencies.
	
	Application Layer:
		Its an Presentation layer. If we decide to change our application architecture from grpc to rest or to graphQl we dont need to update 
		multiple packages then.
		Primary adapters/implementations are written here as we may need to change how we communicate with other microservices 
		EX: we can shift from restTemplate to grpcTemplate in future.
		Application layer will have all dependencies i.e 
			* Domain layer (for domain models and events) , 
			* Contracts (for DTO's and contracts)
			* Infra layer (To get the actual implementations and beans)
	Contracts: 
		These contains context maps , anti-corruption layer libraries and other classes which are required by or from other microservices.
		We cannot have domain dependency in this module because domain and business logics needs to be abstracted from other teams.
		Contracts module will be the only jar shared with other teams to intract with this microservice.
		Contracts module cannot have any dependency and can have enemic models.
	
	Infrastructer Layer:
		This layer will have all I/O configuration and code ex: connecting to database, DAO layers , writing to file system.
		Sending events to messaging queue. 
		Secondary Adapters/implementations are written here as we may need to change underlying databases , messaging queues , etc.
		Infrastructure layer will have only one dependency i.e. Domain Layer.

### Benifits
	* Easier migrations with least business logic / domain impact as domain module will not be touched.
	* Prevents developers to write tightly coupled code and enforce to code for interfaces.
	* Developers can focus on domain modeling and business logic and can delegate application and infra work 
	  which are more of boilerplate codes to work later or to other devs.
		EX: While writing business logic for enquiry aggregate we can just create PORTS/INTERFACES for DAO and SERVICE layers and can 
			implement later once done.
	* Better Unit Testing.