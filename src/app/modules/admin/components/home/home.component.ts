import { Component, OnInit } from '@angular/core';
import {
  faDashboard,
  faLocation,
  faShop,
  faBox,
  faMoneyBill,
  faChartBar,
  faContactBook,
  faHand,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    faDashboard = faDashboard;
    faLocation = faLocation;
    faShop = faShop;
    faBox = faBox;
    faMoneyBill = faMoneyBill;
    faChartBar = faChartBar;
    faContactBook = faContactBook;
    faHand = faHand;

  constructor() { }

  ngOnInit(): void {
  }

}
