import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppConstants } from "../../constants/app.constants";
import { environment } from '../../../environments/environment';
import { Contact } from '../../model/contact.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private http:HttpClient) { }

  getAccountDetails(id: number){
    return this.http.get(environment.rooturl + AppConstants.ACCOUNT_API_URL + "?id="+id,{ observe: 'response',withCredentials: true });
  }

  getAccountTransactions(id: number){
    return this.http.get(environment.rooturl + AppConstants.BALANCE_API_URL+ "?id="+id,{ observe: 'response',withCredentials: true });
  }

  getLoansDetails(id: number){
    return this.http.get(environment.rooturl + AppConstants.LOANS_API_URL+ "?id="+id,{ observe: 'response',withCredentials: true });
  }

  getCardsDetails(id: number){
    return this.http.get(environment.rooturl + AppConstants.CARDS_API_URL+ "?id="+id,{ observe: 'response',withCredentials: true });
  }

  getNoticeDetails(){
    return this.http.get(environment.rooturl + AppConstants.NOTICES_API_URL,{ observe: 'response' });
  }

  /*
  * 원래는 공개 API 이지만, 테스트를 위해 비공개로 전환 (withCredentials: true) 
  * */
  saveMessage(contact : Contact){
    return this.http.post(environment.rooturl + AppConstants.CONTACT_API_URL,contact,{ observe: 'response', withCredentials: true });
  }

}
