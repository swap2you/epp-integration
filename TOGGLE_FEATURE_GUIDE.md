# EPP Response Toggle Feature Guide

## Overview

The EPP Integration now supports **two different approaches** for handling HTML form submission after payment initiation:

1. **Spring Template Approach** (Default) - Uses Thymeleaf template
2. **.NET-Style Direct Response** - Writes HTML directly to HTTP response

## Configuration

### Toggle Setting
```yaml
# application.yml or application-test.yml
epp:
  test:
    use-direct-response: false  # false = Spring template, true = .NET-style
```

## Implementation Details

### Spring Template Approach (`use-direct-response: false`)
- **How it works**: EPP form HTML is passed to Thymeleaf template
- **Template**: `src/main/resources/templates/epp-redirect.html`
- **Process**: Controller → Model → Template → Browser
- **Benefits**: Follows Spring Boot conventions, easy to customize UI

```java
// In TestUIController
model.addAttribute("eppForm", eppForm);
return "epp-redirect"; // Returns template name
```

### .NET-Style Direct Response (`use-direct-response: true`)
- **How it works**: HTML is written directly to HTTP response (like .NET `HttpContext.Response.WriteAsync`)
- **Process**: Controller → HttpServletResponse → Browser (no template)
- **Benefits**: Matches .NET implementation exactly, slightly more performant

```java
// In TestUIController  
response.setContentType("text/html; charset=UTF-8");
response.getWriter().write(eppForm);
return null; // No template needed
```

## Testing Both Approaches

### Test Spring Template Approach (Default)
1. Ensure `use-direct-response: false` in application-test.yml
2. Start application: `mvn spring-boot:run -Dspring-boot.run.profiles=test`
3. Visit: http://localhost:8080/test/form
4. Submit test payment
5. **Expected**: Loading page with spinner, then auto-redirect to EPP

### Test .NET-Style Direct Response
1. Set `use-direct-response: true` in application-test.yml
2. Restart application
3. Submit test payment again
4. **Expected**: Direct HTML output (no loading page), immediate redirect to EPP

## Code Implementation

The toggle is implemented in `TestUIController.submitTest()` method:

```java
// Toggle logic around line 185-195
if (useDirectResponse) {
    // .NET-style approach: Direct response writing
    return handleDirectResponse(eppForm, response);
} else {
    // Spring template approach: Use Thymeleaf template
    return handleTemplateResponse(eppForm, model);
}
```

### Helper Methods

```java
/**
 * .NET-style: Write HTML directly to response
 */
private String handleDirectResponse(String eppForm, HttpServletResponse response) throws IOException {
    response.setContentType("text/html; charset=UTF-8");
    response.getWriter().write(eppForm);
    response.getWriter().flush();
    return null; // No template
}

/**
 * Spring-style: Use template rendering  
 */
private String handleTemplateResponse(String eppForm, Model model) {
    model.addAttribute("eppForm", eppForm);
    return "epp-redirect"; // Template name
}
```

## Comparison with Original .NET Code

### Original .NET Implementation:
```csharp
string html = JsonBuildPostForm(Url, JsonPostData); 
var data = Encoding.UTF8.GetBytes(html);
context.HttpContext.Response.ContentType = "text/html; charset=UTF-8"; 
context.HttpContext.Response.WriteAsync(html);
```

### Java Equivalent (with `use-direct-response: true`):
```java
String eppForm = paymentService.generateEppForm(saleDetails);
response.setContentType("text/html; charset=UTF-8");
response.getWriter().write(eppForm);
response.getWriter().flush();
```

## When to Use Which Approach

### Use Spring Template (`false`) When:
- You want consistent Spring Boot patterns
- You need to customize the loading/redirect page  
- You want easier debugging of the HTML template
- Following Spring Boot best practices

### Use Direct Response (`true`) When:
- You want exact .NET behavior match
- You need maximum performance (skip template rendering)
- You want simpler request/response flow
- Migrating from .NET and need identical behavior

## Default Recommendation

**Keep `use-direct-response: false`** (Spring template approach) for:
- Better maintainability
- Spring Boot convention compliance  
- Easier customization of redirect experience
- Proper separation of concerns

The current implementation **works correctly with both approaches** and the redirect functionality has been **fixed** with improved auto-submit logic.