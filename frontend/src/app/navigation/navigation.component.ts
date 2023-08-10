import { Component, inject } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable, combineLatest } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import * as authReducer from '../auth/auth.reducer';
import { selectAuthLoggedIn, selectAuthUser } from '../auth/auth.selector';
import { User } from '../user/user.model';
import { HttpClient } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';
import { AuthService } from '../auth/auth.service';
import { getLoggedInUserRequested } from '../auth/auth.actions';
import { Router } from '@angular/router';
import { enviorment } from '../enviorment/enviorment';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {
  loggedIn$ = this.store.select(selectAuthLoggedIn);
  loggedIn= false;
  user$ = this.store.select(selectAuthUser);
  user: User;
  profilePicture:any
  constructor(private readonly store:Store<authReducer.State>,private readonly http: HttpClient, private sanitizer: DomSanitizer, private readonly router:Router){
  }
  
  
  private breakpointObserver = inject(BreakpointObserver);

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

    

    ngOnInit(){
      combineLatest([this.loggedIn$ ,this.user$]).subscribe(([loggedIn, user]) =>{
        if(loggedIn && user){
          this.loggedIn = loggedIn
          this.user = user
          this.http.get(`${enviorment.backendHost}/img/pfp/${user.id}/40x40.png`, { responseType: 'blob' } ).subscribe(pfpResponse =>{
            let objectURL = URL.createObjectURL(pfpResponse);
             this.profilePicture = this.sanitizer.bypassSecurityTrustUrl(objectURL);
          })
        }
      })
    }

    navigate(route:string){
      this.router.navigate([route])
    }
}
