/*-------------------------*/

/*Create Controller*/

package nosi.webapps.igrp.pages.transacaoorganica;
import nosi.core.webapp.Controller;
import nosi.core.webapp.Igrp;
import nosi.webapps.igrp.dao.Organization;
import nosi.webapps.igrp.dao.Profile;
import nosi.webapps.igrp.dao.ProfileType;
import nosi.webapps.igrp.dao.Transaction;
import java.io.IOException;
import java.util.ArrayList;


public class TransacaoOrganicaController extends Controller {		

	public void actionIndex() throws IOException{
		String type = Igrp.getInstance().getRequest().getParameter("type");
		String id = Igrp.getInstance().getRequest().getParameter("id");
		if(id!=null && type!=null){
			TransacaoOrganica model = new TransacaoOrganica();
			model.setId(Integer.parseInt(id));
			model.setType(type);
			TransacaoOrganicaView view = new TransacaoOrganicaView(model);
			ArrayList<TransacaoOrganica.Table_1> data = new ArrayList<>();
			Object[] transactions = null;
			if(type.equals("org")){
				transactions = new Organization().getOrgTransaction();
			}else if(type.equals("perfil")){
				ProfileType pt = new ProfileType();
				pt.setId(Integer.parseInt(id));
				ProfileType p = (ProfileType) pt.getOne();
				transactions = new Organization().getPerfilTransaction(p.getOrg_fk());
			}
			for(Object obj:transactions){
				Transaction t = (Transaction) obj;
				TransacaoOrganica.Table_1 table = new TransacaoOrganica().new Table_1();
				table.setTransacao(t.getId());
				table.setDescricao(t.getDescr());
				Profile prof = new Profile();
				prof.setOrg_fk(Integer.parseInt(id));
				prof.setProf_type_fk(1);
				prof.setUser_fk(1);
				prof.setType_fk(t.getId());
				if(type.equals("org")){
					prof.setType("TRANS");
				}else if(type.equals("perfil")){
					ProfileType pt = new ProfileType();
					pt.setId(Integer.parseInt(id));
					ProfileType p = (ProfileType) pt.getOne();
					prof.setOrg_fk(p.getOrg_fk());
					prof.setProf_type_fk(1);
					prof.setType("TRANS_PROF");	
				}
				if(prof.getOne()!=null && ((Profile)prof.getOne()).getType_fk()==t.getId()){
					table.setTransacao_check(t.getId());
				}else{
					table.setTransacao_check(-1);
				}
				data.add(table);
			}
			view.table_1.addData(data);
			this.renderView(view);
		}
	}

	public void actionGravar() throws IOException, IllegalArgumentException, IllegalAccessException{
		String id = Igrp.getInstance().getRequest().getParameter("id");
		String type = Igrp.getInstance().getRequest().getParameter("type");
		if(Igrp.getInstance().getRequest().getMethod().toUpperCase().equals("POST") && type!=null && id!=null){
			TransacaoOrganica model = new TransacaoOrganica();
			model.load();
			Profile profD = new Profile();
			if(type.equals("org")){
				profD.setOrg_fk(Integer.parseInt(id));
				profD.setType("TRANS");
				profD.setProf_type_fk(1);
				profD.setUser_fk(1);
				profD.deleteAllOrgProfile();
			}else if(type.equals("perfil")){
				ProfileType pt = new ProfileType();
				pt.setId(Integer.parseInt(id));
				ProfileType p = (ProfileType) pt.getOne();
				profD.setOrg_fk(p.getOrg_fk());
				profD.setType("TRANS_PROF");
				profD.setUser_fk(1);
				profD.setProf_type_fk(Integer.parseInt(id));
				profD.deleteAllPerfilProfile();
			}
			
			for(String x:Igrp.getInstance().getRequest().getParameterValues("p_transacao")){
				Profile prof = new Profile();
				if(type.equals("org")){
					prof.setOrg_fk(Integer.parseInt(id));
					prof.setType("TRANS");
					prof.setType_fk(Integer.parseInt(x.toString()));
					prof.setProf_type_fk(1);
					prof.setUser_fk(1);
				}else if(type.equals("perfil")){
					ProfileType pt = new ProfileType();
					pt.setId(Integer.parseInt(id));
					ProfileType p = (ProfileType) pt.getOne();
					prof.setOrg_fk(p.getOrg_fk());
					prof.setType("TRANS_PROF");
					prof.setType_fk(Integer.parseInt(x.toString()));
					prof.setProf_type_fk(Integer.parseInt(id));
					prof.setUser_fk(1);
				}
				prof.insert();
			}
			Igrp.getInstance().getFlashMessage().addMessage("success", "Opera��o realizada com sucesso");
		}
		this.redirect("igrp", "TransacaoOrganica", "index","id="+id+"&type="+type);
	}
	
	public void actionVoltar() throws IOException{
			this.redirect("igrp","TransacaoOrganica","index");
	}
	
}
