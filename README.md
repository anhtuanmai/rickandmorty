# Android Mobile Application (Kotlin + Clean Architecture)

```mermaid
block
    columns 3
    
    space:1
    Presentation["Presentation Layer (UI)"]
    space:4
    
    Domain["Domain Layer"]
    space:1
    Data["Data Layer"]
    
    Presentation --> Domain
    Presentation --> Data  
    Data --> Domain
```
