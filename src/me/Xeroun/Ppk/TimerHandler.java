package me.Xeroun.Ppk;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TimerHandler {

// ====================================================================================================
	//Setup
		
	public TimerHandler(Plugin plugin){
		this.plugin = plugin;
		data = new HashMap<Object, Timer>();
		startTimers();
	}
		
// ====================================================================================================
	// Variables
	
	Plugin plugin;
	private boolean over = true;
	private HashMap<Object, Timer> data;	
	
// ====================================================================================================
	
	public Timer getTimer(Object object){
		return data.get(object);
	}
	
	public void addTimer(Object object){
		Timer timer = new Timer(object, this);
		data.put(object, timer);
	}
	
	private void startTimers(){
		if(over == true){
			over = false;
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
				@Override
				public void run() {
					if(data.keySet() != null){
						for(Object obj : data.keySet()){
							Timer timer = data.get(obj);
							
							if(timer.isRunning()){
								if(data.get(obj).getDuration() != -2){
									timer.removeDuration(1);
									data.put(obj, timer);
									
									if(data.get(obj).getDuration() <= 0){
										data.get(obj).stop();
									}else{
										onRun(obj);
									}
								}
								
								

							}
						}
					}
				}
			}, 0, 20L) ;
		}
	}
	
	public void clear(){
		data.clear();
	}
	
	public void onRun(Object object){}
	public void onEnd(Object object){}
	public void onStart(Object object){}
}

class Timer{
	
	public Timer (Object obj, TimerHandler th){
		this.object = obj;
		this.th = th;
	}
	
	private TimerHandler th;
	private Object object;
	private boolean isRun = false;
	private int duration = -2;
	
	public Object getObject(){
		return object;
	}
	
	public Integer getDuration(){
		return duration;
	}
	
	public boolean isRunning(){
		return isRun;
	}
	
	public void run(){
		
		if(isRun == true) return;
		
		isRun = true;
		th.onRun(object);
	}

	public void stop(){
		
		if(isRun == false) return;
		
		isRun = false;
		th.onEnd(object);
	}
	
	public void addDuration(Integer i){
		duration += i;
	}
	
	public void removeDuration(Integer i){
		duration -= i;
	}
	
	public void setDuration(Integer i){
		duration = i;
	}
	
}