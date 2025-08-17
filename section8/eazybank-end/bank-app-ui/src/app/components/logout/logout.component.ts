import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user.model';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  user = new User();
  constructor(private router : Router) {

  }

  ngOnInit(): void {
    window.sessionStorage.setItem("userdetails","");
    /*
    * 테스트로 잠깐 막아둠
    * */
    // window.sessionStorage.setItem("XSRF-TOKEN","");
    this.router.navigate(['/login']);
  }


}
