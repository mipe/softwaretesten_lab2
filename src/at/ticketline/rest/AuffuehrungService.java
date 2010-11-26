package at.ticketline.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import at.ticketline.dao.interfaces.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Reihe;

import com.sun.jersey.spi.inject.Inject;

@Component
@Scope("request")
@Path("auffuehrung")
public class AuffuehrungService {
	
	private AuffuehrungDao auffuehrungDao;

	
	@GET
	@Path("list")
	@Produces("application/json")
	public JSONArray getAuffuehrungen() throws Exception {
		JSONArray toReturn = new JSONArray();
		List<Auffuehrung> list = this.auffuehrungDao.findAll();
		for (Auffuehrung a : list){
			toReturn.put(JSONHelper.fromAuffuehrung(a));
		}
		return toReturn;
	}
	
	@GET
	@Produces("application/json")
	@Path("{id}")
	@Transactional
	public JSONObject getAuffuehrung(@PathParam("id") Integer id) throws Exception {
		Auffuehrung a = this.auffuehrungDao.findById(id);
		JSONObject toReturn = new JSONObject();
		if (a == null) {
			return toReturn;
		} else {
			toReturn = JSONHelper.fromAuffuehrung(a);
			
		}
		
		JSONObject saal = JSONHelper.fromSaal(a.getSaal());
		
		JSONArray reihen = new JSONArray();
		for (Reihe r : a.getSaal().getReihen()) {
			JSONObject reihe = JSONHelper.fromReihe(r);
			reihen.put(reihe);
		}
		saal.put("reihen", reihen);
		toReturn.put("saal", saal);
		
		JSONArray plaetze = new JSONArray();
		for (Platz p : a.getPlaetze()) {
			plaetze.put(JSONHelper.fromPlatz(p));
		}
		toReturn.put("plaetze", plaetze);
    	return toReturn;

	}

	
	@Inject("auffuehrungDao")
	public void setAuffuehrungDao(AuffuehrungDao auffuehrungDao) {
		this.auffuehrungDao = auffuehrungDao;
	}
	
	public AuffuehrungDao getAuffuehrungDao() {
		return auffuehrungDao;
	}
	
}
