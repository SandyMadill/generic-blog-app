import { Directive, OnInit, OnDestroy, Input, Output, EventEmitter, ElementRef } from "@angular/core";
import { Router, NavigationEnd } from "@angular/router";
import { Subscription, filter } from "rxjs";

@Directive({
    selector: '[appIntersection]',
  })
  export class IntersectionDirective implements OnInit, OnDestroy {
    observer: IntersectionObserver;
    @Input() rootMargin = `0px`;
    @Output() scrollEnd: EventEmitter<any> = new EventEmitter();
    constructor(private el: ElementRef,private router:Router) {
    }
    ngOnInit() {
      this.observer = new IntersectionObserver(
        (entries) => {
          if (entries[0].isIntersecting === true) {
            this.scrollEnd.emit();
          }
        },
        {
          threshold: 1,
        }
      );
  
      this.observer.observe(this.el.nativeElement as HTMLElement);
    }
    ngOnDestroy(): void {
      this.observer.disconnect();
    }
  }