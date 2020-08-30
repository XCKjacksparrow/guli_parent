package per.xck.eduservice.controller;

import per.xck.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {

    // login
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    // info
    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","admin").data("name","admin").data("avatar","https://avatars2.githubusercontent.com/u/39412488?s=460&u=9a602cf01a1390642f1363103e3c2ac55df3d9a6&v=4");
    }
}
