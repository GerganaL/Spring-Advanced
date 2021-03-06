package bg.softuni.musicdb.web;

import bg.softuni.musicdb.model.binding.UserRegistrationBindingModel;
import bg.softuni.musicdb.model.service.UserRegistrationServiceModel;
import bg.softuni.musicdb.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @ModelAttribute("registrationBindingModel")
    public UserRegistrationBindingModel createBindingModel(){
        return new UserRegistrationBindingModel();
    }

    @GetMapping("/login")
    public String login() {
        //todo... something very complex happens here

      //  throw new NullPointerException();
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerAndLoginUser(
            @Valid UserRegistrationBindingModel registrationBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registrationBindingModel", registrationBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registrationBindingModel", bindingResult);

            return "redirect:/users/register";
        }

        if (userService.usernameExists(registrationBindingModel.getUsername())) {
            redirectAttributes.addFlashAttribute("registrationBindingModel", registrationBindingModel);
            redirectAttributes.addFlashAttribute("userExists", true);

            return "redirect:/users/register";
        }

        UserRegistrationServiceModel userServiceModel = modelMapper
                .map(registrationBindingModel, UserRegistrationServiceModel.class);

        userService.registerAndLoginUser(userServiceModel);

        return "redirect:/home";
    }



    @PostMapping("/login-error")
    public ModelAndView failedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                    String username) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("bad_credentials", true);
        modelAndView.addObject("username", username);

        modelAndView.setViewName("/login");
        return modelAndView;
    }


}
;