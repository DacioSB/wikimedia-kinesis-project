# Wikimedia Kinesis Streaming Application

![Wikimedia](https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Wikimedia-logo.png/768px-Wikimedia-logo.png)

This project demonstrates a data engineering pipeline that streams real-time Wikimedia changes using the Wikimedia EventStreams API and processes them with AWS Kinesis. The application includes a producer that streams data to Kinesis and multiple consumers that process the data using different approaches, including enhanced fan-out.

## Features

- **Wikimedia Event Streaming**: Streams real-time changes from Wikimedia using the EventStreams API.
- **AWS Kinesis Integration**: Publishes and consumes data using AWS Kinesis.
- **Enhanced Fan-Out Consumer**: Implements Kinesis enhanced fan-out for high-throughput data processing.
- **JSON Parsing**: Parses Wikimedia JSON events into Java objects for structured processing.
- **Logging**: Uses SLF4J for logging.

## Prerequisites

1. **AWS Account**: Ensure you have an AWS account with access to Kinesis.
2. **Java Development Kit (JDK)**: Version 17 or higher.
3. **Maven**: For building the project.
4. **AWS Credentials**: Add your AWS access key and secret key to the `application.properties` file.

## Configuration

Update the `src/main/resources/application.properties` file with your AWS credentials:

```properties
aws.secret.key=YOUR_SECRET_KEY
aws.access.key=YOUR_ACCESS_KEY
```

## How It Works

Producer
The producer (`WikimediaMainProducer`) streams real-time Wikimedia changes from the EventStreams API to an AWS Kinesis stream.

Consumer
1. **Standard Consumer**: WikimediaConsumer reads records from the Kinesis stream using the standard Kinesis API.
2. **Enhanced Fan-Out Consumer**: WikimediaFanOutConsumer uses Kinesis enhanced fan-out for high-throughput data processing.

Data Representation
* `WikimediaRepresentation`: Represents the main structure of a Wikimedia event.
* `WikimediaMetaRepresentation`: Represents metadata for a Wikimedia event.

Event Handling
`WikimediaChangeHandler`: Handles incoming Wikimedia events and publishes them to the Kinesis stream.

## Dependencies

The project uses the following dependencies:

* **AWS SDK**: For interacting with AWS Kinesis.
* **Jackson**: For JSON parsing.
* **SLF4J**: For logging.
* **OkHttp**: For HTTP requests.
* **LaunchDarkly EventSource**: For consuming Wikimedia EventStreams.
* **Building and Running**
* **Build the Project**:

## Building and Running

Build the Project:
```bash
mvn clean install
```
Run the Producer:
```bash
java -cp target/kinesis-wikimedia-y.jar com.kinesis.wikimedia.pluralsight.WikimediaMainProducer
```

Run the Consumer:

```bash
java -cp target/kinesis-wikimedia-y.jar com.kinesis.wikimedia.pluralsight.WikimediaConsumer
```

Run the Enhanced Fan-Out Consumer:

```bash
java -cp target/kinesis-wikimedia-y.jar com.kinesis.wikimedia.pluralsight.WikimediaFanOutConsumer
```

## Logging
Logs are generated using SLF4J. You can configure the logging level in the pom.xml file by modifying the slf4j-simple dependency.

## Future Improvements
* Add support for dynamic shard discovery.
* Implement error handling and retries for Kinesis operations.
* Integrate with a database or data lake for long-term storage.
  
## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
Wikimedia EventStreams API
AWS Kinesis
Pluralsight tutorials
