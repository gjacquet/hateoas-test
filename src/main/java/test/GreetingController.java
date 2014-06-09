package test;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/greeting",
        consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json", "application/hal+json" },
        produces =  { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json", "application/hal+json" })
public class GreetingController {
    private static final String TEMPLATE = "Hello, %s!";

    @RequestMapping(value = "/{id}")
    public HttpEntity<Greeting> greeting(@PathVariable("id") String greetingId) {
        Greeting greeting = new Greeting(String.format(TEMPLATE, greetingId));
        greeting.add(linkTo(methodOn(GreetingController.class).greeting(greetingId)).withSelfRel());

        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/")
    public HttpEntity<PagedResources<Resource<Greeting>>> greeting(Pageable pageable, PagedResourcesAssembler<Greeting> pagedResourcesAssembler) {
        Greeting greeting1 = new Greeting(String.format(TEMPLATE, "Guillaume"));
        greeting1.add(linkTo(methodOn(GreetingController.class).greeting("Guillaume")).withSelfRel());

        Greeting greeting2 = new Greeting(String.format(TEMPLATE, "World"));
        greeting2.add(linkTo(methodOn(GreetingController.class).greeting("World")).withSelfRel());

        Page<Greeting> page = new PageImpl<Greeting>(Arrays.asList(greeting1, greeting2), pageable, 35L);

        PagedResources<Resource<Greeting>> resources = pagedResourcesAssembler.toResource(page);

        return new ResponseEntity<PagedResources<Resource<Greeting>>>(resources, HttpStatus.OK);
    }
}
