/*-------------------------*/

/*Create Controller*/

package nosi.webapps.igrp.pages.env;
import java.io.IOException;
/*import nosi.webapps.red.teste.Teste;
import nosi.webapps.red.teste.Teste;
*/
import java.io.PrintWriter;

import nosi.core.config.Config;
import nosi.core.webapp.Controller;
import nosi.core.webapp.FlashMessage;
import nosi.core.webapp.Igrp;
import nosi.core.webapp.RParam;
import nosi.core.webapp.helpers.FileHelper;
import nosi.core.xml.XMLWritter;
import nosi.webapps.igrp.dao.Application;

public class EnvController extends Controller {		

	public void actionIndex() throws IOException{
		Env model = new Env();
		EnvView view = new EnvView(model);
		this.renderView(view);
	}

	public void actionGravar() throws IOException, IllegalArgumentException, IllegalAccessException{
		Env model = new Env();
		if(Igrp.getInstance().getRequest().getMethod().toUpperCase().equals("POST")){
			model.load();
			Application app = new Application();
			app.setAction_fk(model.getAction_fk());
			app.setApache_dad(model.getApache_dad());
			app.setDad(model.getDad());
			app.setDescription(model.getDescription());
			app.setFlg_external(model.getFlg_external());
			app.setFlg_old(model.getFlg_old());
			app.setHost(model.getHost());
			app.setImg_src(model.getImg_src());
			app.setLink_center(model.getLink_center());
			app.setLink_menu(model.getLink_menu());
			app.setName(model.getName());
			app.setStatus(model.getStatus());
			app.setTemplates(model.getTemplates());
			if(app.insert() && FileHelper.createDiretory(Config.getBasePathClass()+"nosi"+"/"+"webapps"+"/"+app.getDad().toLowerCase()+"/"+"pages") && FileHelper.save(Config.getBasePathClass()+"nosi"+"/"+"webapps"+"/"+app.getDad().toLowerCase()+"/"+"pages"+"/"+"defaultpage", "DefaultPageController.java",Config.getDefaultPageController(app.getDad().toLowerCase(), app.getName())) && FileHelper.compile(Config.getBasePathClass()+"/"+"nosi"+"/"+"webapps"+"/"+app.getDad().toLowerCase()+"/"+"pages"+"/"+"defaultpage", "DefaultPageController.java")){
				if(FileHelper.fileExists(Config.getProject_loc()) && FileHelper.createDiretory(Config.getProject_loc()+"/src/nosi"+"/"+"webapps/"+app.getDad().toLowerCase()+"/pages/defaultpage")){
					FileHelper.save(Config.getProject_loc()+"/src/nosi"+"/"+"webapps"+"/"+app.getDad().toLowerCase()+"/"+"pages/defaultpage", "DefaultPageController.java",Config.getDefaultPageController(app.getDad().toLowerCase(), app.getName()));
				}
				Igrp.getInstance().getFlashMessage().addMessage("success", "Opera��o efetuada com sucesso!");
				this.redirect("igrp", "lista-env","index");
				return;
			}else{
				Igrp.getInstance().getFlashMessage().addMessage("error", "Falha ao efetuar esta opera��o!");
			}
		}
		this.redirect("igrp", "env", "index");
	}
	
	public void actionVoltar() throws IOException{
		this.redirect("igrp", "lista-env","index");
	}
	
	
	
	//App list I have access to
	public PrintWriter actionMyApps() throws IOException{
		Igrp.getInstance().getResponse().setContentType("text/xml");
		Igrp.getInstance().getResponse().getWriter().append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		Object[] myApp = new Application().getMyApp();
		Object[] otherApp = new Application().getOtherApp();
		XMLWritter xml_menu = new XMLWritter();
		xml_menu.startElement("applications");
		if(myApp.length>0){
			System.out.println(myApp);
			xml_menu.setElement("title", "Minhas Aplica��es");
		}
		if(otherApp.length>0){
			xml_menu.setElement("subtitle", "Outras Aplica��es");
		}
		xml_menu.setElement("link_img", Config.getLinkImg());
		int i=1;
		for(Object obj:myApp){
			Application app = (Application) obj;
			xml_menu.startElement("application");
			xml_menu.writeAttribute("available", "yes");
			xml_menu.setElement("link", "webapps?r="+app.getDad().toLowerCase()+"/default-page/index&amp;title="+app.getName());
			xml_menu.setElement("img", "app_casacidadao.png");
			xml_menu.setElement("title", app.getName());
			xml_menu.setElement("num_alert", ""+i);
			xml_menu.endElement();
			i++;
		}
		for(Object obj:otherApp){
			Application app = (Application) obj;
			xml_menu.startElement("application");
			xml_menu.writeAttribute("available", "no");
			xml_menu.setElement("link", "webapps?r="+app.getDad().toLowerCase()+"/default-page/index&amp;title="+app.getName());
			xml_menu.setElement("img", "app_casacidadao.png");
			xml_menu.setElement("title", app.getName());
			xml_menu.setElement("num_alert", ""+i);
			xml_menu.endElement();
			i++;
		}
		xml_menu.endElement();
		return Igrp.getInstance().getResponse().getWriter().append(xml_menu.toString());
	}
	
	
	public void actionEditar(@RParam(rParamName = "id") String idAplicacao) throws IllegalArgumentException, IllegalAccessException, IOException{
		Env model = new Env();
		
		Application aplica_db = new Application();
		aplica_db.setId(Integer.parseInt(idAplicacao));
		aplica_db = (Application) aplica_db.getOne();
		
		model.setDad(aplica_db.getDad()); // field dad is the same a Schema
		model.setName(aplica_db.getName());
		model.setDescription(aplica_db.getDescription());
		model.setAction_fk(aplica_db.getAction_fk());
		model.setApache_dad(aplica_db.getApache_dad());
		model.setFlg_external(aplica_db.getFlg_external());
		model.setImg_src(aplica_db.getImg_src());
		model.setStatus(aplica_db.getStatus());
		model.setFlg_old(aplica_db.getFlg_old());
		model.setLink_center(aplica_db.getLink_center());
		model.setLink_menu(aplica_db.getLink_menu());
		model.setTemplates(aplica_db.getTemplates());
		model.setHost(aplica_db.getHost());
		
		if(Igrp.getInstance().getRequest().getMethod().equals("POST")){
			model.load();
			
			aplica_db.setDad(model.getDad());
			aplica_db.setName(model.getName());
			aplica_db.setImg_src(model.getImg_src());
			aplica_db.setDescription(model.getDescription());
			aplica_db.setAction_fk(model.getAction_fk());
			aplica_db.setStatus(model.getStatus());
			aplica_db.setFlg_old(model.getFlg_old());
			aplica_db.setLink_menu(model.getLink_menu());
			aplica_db.setLink_center(model.getLink_center());
			aplica_db.setApache_dad(model.getApache_dad());
			aplica_db.setTemplates(model.getTemplates());
			aplica_db.setHost(model.getHost());
			aplica_db.setFlg_external(model.getFlg_external());
			
			if(aplica_db.update()){
				Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.SUCCESS, "Aplica��o Actualizada com sucesso!!");
				this.redirect("igrp", "lista-env", "index");
			}else{
				Igrp.getInstance().getFlashMessage().addMessage(FlashMessage.ERROR, "Ocorre um Erro ao tentar Actualizar a Aplica��o!!");
			}
			
		}
		
		
		EnvView view = new EnvView(model);
		view.sectionheader_1_text.setValue("Gest�o de Aplica��o - Actualizar");
		view.btn_gravar.setLink("editar&id=" + idAplicacao);
		this.renderView(view);
	}
	
}







