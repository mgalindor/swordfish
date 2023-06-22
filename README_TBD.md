# swordfish
Is a hexagonal base project design as a template for future projects.


It include
- AOP Transaction Manager
- @Behavior annptation to use instead @Service
- Component Scan looking for @Behavior annotated classes
- Logging Aspect to log a blue print when a method is called and it gets the result
- RestTemplate RequestFactory configuration for
  - URLConnection
  - Apache HTTPClient
  - OkHttp Client
- Configuracion de Jackson Money to handle currency objects at controllers
- Enum validations for Request objects 
- Basic Controller Advice for mos common exceptions
