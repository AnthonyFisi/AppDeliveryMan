package com.example.motorizadoyego.Util;

import android.location.Location;

import com.example.motorizadoyego.Repository.Modelo.Delivery_Pedido;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


public class RxBus {
    private static RxBus mInstance;
    public static RxBus getInstance() {
        if (mInstance == null) {
            mInstance = new RxBus();
        }
        return mInstance;
    }
    private RxBus() {
    }

    private PublishSubject<String> publisher = PublishSubject.create();

    private PublishSubject<Location> publisherLocation= PublishSubject.create();

    private PublishSubject<Boolean> publisherStep2Fragment=PublishSubject.create();

    private PublishSubject<Delivery_Pedido> publisherStep3Fragment=PublishSubject.create();

    private PublishSubject<Delivery_Pedido> publisherStepPictureFragment=PublishSubject.create();

    private PublishSubject<Delivery_Pedido> publisherStep4Fragment=PublishSubject.create();

    private PublishSubject<Delivery_Pedido> publisherStep5Fragment=PublishSubject.create();

    private PublishSubject<Delivery_Pedido> publisherStep6Fragment=PublishSubject.create();

    private PublishSubject<HashMap<String,String>> publisherDistanceAndTime=PublishSubject.create();

    private PublishSubject<Boolean> publisherPermission=PublishSubject.create();

    private PublishSubject<Boolean> publisherCloseListPedido=PublishSubject.create();

    private PublishSubject<Delivery_Pedido> publisherListPedidoFragment=PublishSubject.create();


    public void publishDistanceAndTime(HashMap<String,String> data){
        publisherDistanceAndTime.onNext(data);
    }

    public Observable<HashMap<String,String>> listenDistanceAndTime(){
        return publisherDistanceAndTime;
    }

    public void publish(String event) {
        System.out.println(event + "ESTE EVENTO NO LLEGA");
        publisher.onNext(event);
    }
    // Listen should return an Observable

    public Observable<String> listen() {
        System.out.println("SE ESCUCHA EL PUTO EVENTO");
        return publisher;
    }

    public  void publishLocation(Location location){

        publisherLocation.onNext(location);
    }

    public Observable<Location> listenLocation(){
        return publisherLocation;
    }

    public void publishStep2Fragment(boolean request){
        publisherStep2Fragment.onNext(request);
    }

    public Observable<Boolean> listenStep2Fragment(){
        return publisherStep2Fragment;
    }


    public void publishStep3Fragment(Delivery_Pedido request){
        publisherStep3Fragment.onNext(request);
    }

    public Observable<Delivery_Pedido> listenStep3Fragment(){
        return publisherStep3Fragment;
    }



    public void publishStepPictureFragment(Delivery_Pedido request){
        publisherStepPictureFragment.onNext(request);
    }

    public Observable<Delivery_Pedido> listenStepPictureFragment(){
        return publisherStepPictureFragment;
    }



    public void publishStep4Fragment(Delivery_Pedido request){
        publisherStep4Fragment.onNext(request);
    }

    public Observable<Delivery_Pedido> listenStep4Fragment(){
        return publisherStep4Fragment;
    }


    public void publishStep5Fragment(Delivery_Pedido request){
        publisherStep5Fragment.onNext(request);
    }

    public Observable<Delivery_Pedido> listenStep5Fragment(){
        return publisherStep5Fragment;
    }


    public void publishStep6Fragment(Delivery_Pedido request){
        publisherStep6Fragment.onNext(request);
    }

    public Observable<Delivery_Pedido> listenStep6Fragment(){
        return publisherStep6Fragment;
    }



    public void publishPermission(boolean request){
        publisherPermission.onNext(request);
    }

    public Observable<Boolean> listenPermission(){
        return publisherPermission;
    }

    public void publishListPedido(boolean request){
        publisherCloseListPedido.onNext(request);
    }

    public Observable<Boolean> listenListPedido(){
        return publisherCloseListPedido;
    }


    public void publishListPedidoFragment(Delivery_Pedido request){
        publisherListPedidoFragment.onNext(request);
    }

    public Observable<Delivery_Pedido> listenListPedidoFragment(){
        return publisherListPedidoFragment;
    }



}