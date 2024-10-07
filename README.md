# ODRE Framework
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser/badge.png?version=0.1.2&style=flat&gav=true)](https://central.sonatype.com/artifact/io.github.odre-framework/odre-core) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Quick install

#### Install
Install latest version from  [maven central repository](https://central.sonatype.com/artifact/io.github.odre-framework/odre-core)

```xml
<dependency>
    <groupId>io.github.odre-framework</groupId>
    <artifactId>odre-core</artifactId>
    <version>0.1.2</version>
</dependency>
```

#### Enforce ODRL policies

````java
String policy = // ODRL policy goes here

Map<String, Object> interpolations = new HashMap<>();
ODRE odre = new ODRE();

Map<String, String> usage = odre.enforce(policy, interpolations);
````

## Supported features

### ODRL operantors

| Operators | Implementation status | # |
|--|--| -- |
| [eq](http://www.w3.org/ns/odrl/2/eq) | supported  | &check; |
| [gt](http://www.w3.org/ns/odrl/2/gt)  | supported  | &check; |
| [gteq](http://www.w3.org/ns/odrl/2/gteq) | supported  | &check; |
| [lt](http://www.w3.org/ns/odrl/2/lt)  | supported  | &check; |
| [lteq](http://www.w3.org/ns/odrl/2/lteq)  | supported  | &check; |
| [neq](http://www.w3.org/ns/odrl/2/neq)  | supported  | &check; |
| [hasPart](http://www.w3.org/ns/odrl/2/hasPart) | unsupported  | &cross; |
| [isA](http://www.w3.org/ns/odrl/2/isA) | unsupported  | &cross; |
| [isAllOf](http://www.w3.org/ns/odrl/2/isAllOf) | unsupported  | &cross; |
| [isAnyOf](http://www.w3.org/ns/odrl/2/isAnyOf) | unsupported  | &cross; |
| [isNoneOf](http://www.w3.org/ns/odrl/2/isNoneOf) | unsupported  | &cross; |
| [isPartOf](http://www.w3.org/ns/odrl/2/isPartOf) | unsupported  | &cross; |

### ODRL operands
* The available implemented [Left Operands](http://www.w3.org/ns/odrl/2/LeftOperand) from those specified in the [ODRL Vocabulary & Expression 2.2](https://www.w3.org/ns/odrl/2/) are the following:

| Left Operands                                                                               | Implementation status | # |
|---------------------------------------------------------------------------------------------|--| -- |
| [absolutePosition](http://www.w3.org/ns/odrl/2/dateTimeabsolutePosition)                 | unsupported | &cross; |
| [absoluteSize](http://www.w3.org/ns/odrl/2/dateTimeabsoluteSize)                         | unsupported | &cross; |
| [absoluteSpatialPosition](http://www.w3.org/ns/odrl/2/dateTimeabsoluteSpatialPosition)   | unsupported | &cross; |
| [absoluteTemporalPosition](http://www.w3.org/ns/odrl/2/dateTimeabsoluteTemporalPosition) | unsupported | &cross; |
| [count](http://www.w3.org/ns/odrl/2/dateTimecount)                                       | unsupported | &cross; |
| [dateTime](http://www.w3.org/ns/odrl/2/dateTime)                                 | supported | &check; |
| [delayPeriod](http://www.w3.org/ns/odrl/2/delayPeriod)                           | unsupported | &cross; |
| [deliveryChannel](http://www.w3.org/ns/odrl/2/deliveryChannel)                   | unsupported | &cross; |
| [device](http://www.w3.org/ns/odrl/2/device)                                     | unsupported | &cross; |
| [elapsedTime](http://www.w3.org/ns/odrl/2/elapsedTime)                           | unsupported | &cross; |
| [event](http://www.w3.org/ns/odrl/2/event)                                       | unsupported | &cross; |
| [fileFormat](http://www.w3.org/ns/odrl/2/fileFormat)                             | unsupported | &cross; |
| [industry](http://www.w3.org/ns/odrl/2/industry)                                 | unsupported | &cross; |
| [language](http://www.w3.org/ns/odrl/2/language)                                 | unsupported | &cross; |
| [media](http://www.w3.org/ns/odrl/2/media)                                       | unsupported | &cross; |
| [meteredTime](http://www.w3.org/ns/odrl/2/meteredTime)                           | unsupported | &cross; |
| [payAmount](http://www.w3.org/ns/odrl/2/payAmount)                               | unsupported | &cross; |
| [percentage](http://www.w3.org/ns/odrl/2/percentage)                             | unsupported | &cross; |
| [product](http://www.w3.org/ns/odrl/2/product)                                   | unsupported | &cross; |
| [purpose](http://www.w3.org/ns/odrl/2/purpose)                                   | unsupported | &cross; |
| [recipient](hhttp://www.w3.org/ns/odrl/2/recipient)                               | unsupported | &cross; |
| [relativePosition](http://www.w3.org/ns/odrl/2/relativePosition)                 | unsupported | &cross; |
| [relativeSize](http://www.w3.org/ns/odrl/2/relativeSize)                         | unsupported | &cross; |
| [relativeSpatialPosition](http://www.w3.org/ns/odrl/2/relativeSpatialPosition)   | unsupported | &cross; |
| [relativeTemporalPosition](http://www.w3.org/ns/odrl/2/relativeTemporalPosition) | unsupported | &cross; |
| [resolution](http://www.w3.org/ns/odrl/2/resolution)                             | unsupported | &cross; |
| [spatial](hhttp://www.w3.org/ns/odrl/2/spatial)                                   | unsupported | &cross; |
| [spatialCoordinates](http://www.w3.org/ns/odrl/2/spatialCoordinates)             | unsupported | &cross; |
| [system](http://www.w3.org/ns/odrl/2/system)                                     | unsupported | &cross; |
| [systemDevice](http://www.w3.org/ns/odrl/2/systemDevice)                         | unsupported | &cross; |
| [timeInterval](http://www.w3.org/ns/odrl/2/timeInterval)                         | unsupported | &cross; |
| [unitOfCount](http://www.w3.org/ns/odrl/2/unitOfCount)                           | unsupported | &cross; |
| [version](http://www.w3.org/ns/odrl/2/version)                                   | unsupported | &cross; |
| [virtualLocation](http://www.w3.org/ns/odrl/2/virtualLocation)                   | unsupported | &cross; |


* The available implemented [Right Operands](http://www.w3.org/ns/odrl/2/RightOperand) from those specified in the [ODRL Vocabulary & Expression 2.2](https://www.w3.org/ns/odrl/2/) are the following:

| Right Operands | Implementation status | # |
|--|--| -- |
| [policyUsage](http://www.w3.org/ns/odrl/2/policyUsage) | unsupported  | &cross; |

### ODRL actions

* The available implemented [Right Operands](http://www.w3.org/ns/odrl/2/RightOperand) from those specified in the [ODRL Vocabulary & Expression 2.2](https://www.w3.org/ns/odrl/2/) are the following:

| Actions                                                           | Implementation status | # |
|-------------------------------------------------------------------|--| -- |
| [policyUsage](https://www.w3.org/TR/odrl-vocab/#term-policyUsage) | unsupported  | &cross; |




### BibTeX

```bibtex
@misc{cimmino2024opendigitalrightsenforcement,
      title={Open Digital Rights Enforcement Framework (ODRE): from descriptive to enforceable policies}, 
      author={Andrea Cimmino and Juan Cano-Benito and Raúl García-Castro},
      year={2024},
      eprint={2409.17602},
      archivePrefix={arXiv},
      primaryClass={cs.CR},
      url={https://arxiv.org/abs/2409.17602}, 
}
```
