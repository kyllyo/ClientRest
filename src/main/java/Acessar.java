import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;


public class Acessar {
	
	//private static String urlServico = "http://localhost:8080/saj-ged/api/processo/documentos/";
	private static String urlServicoPecas = "http://localhost:8080/saj-ged/api/integra/pecas/";
	//private static String userSenha = "user:senha";
	private static String userSenha = "usr_ws_ged_des:@usr$ws#ged%des04";	
	
//	private int BUFFER_SIZE = 8192;

	
	
	/**
	 * Transforma um input Stream em um array de bytes. 
	 * @param source InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] read(InputStream source) throws IOException {

	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int nRead;
	    byte[] data = new byte[1024];
	    while ((nRead = source.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, nRead);
	    }
	 
	    buffer.flush();
	    
	    byte[] byteArray = buffer.toByteArray();
	    
	    buffer.close();
	    
	    return byteArray;
    }
	
	private static void trataErro(HttpResponse response) throws IllegalStateException, IOException{
		if (response.getStatusLine().getStatusCode() != Response.Status.OK.getStatusCode()
				&& response.getStatusLine().getStatusCode() != Response.Status.CREATED.getStatusCode()) {
			String bodyResponse = new String(read(response.getEntity().getContent()));
			throw new RuntimeException("Erro! "+response.getStatusLine().getStatusCode()+" "+bodyResponse);
		}
	}
	
	private static void incluirPeca(){
		try{
			
			/*
			 * Chamando o metodo para incluir peca.
			 */
			HttpPost postRequest = new HttpPost(urlServicoPecas);
			postRequest.addHeader("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04");
			
			MultipartEntity part = new MultipartEntity();
			
		    File f = new File("/home/87652404304/meus.arquivos/docs-teste-saj-ged/manual4.pdf");
		    InputStreamBody body = new InputStreamBody(Files.newInputStream(f.toPath()),f.getName());
		    
		    part.addPart("processo", new StringBody("300"));
		    part.addPart("tipo_arquivo", new StringBody("7"));
		    part.addPart("dados", body);
		    
		    postRequest.setEntity(part);
		    
            HttpResponse response = new DefaultHttpClient().execute(postRequest);
			
            trataErro(response);			

			InputStream stream = response.getEntity().getContent();
			
			System.out.println(new Integer(new String(read(stream))));
			
			/*
			 * Fim da chamada de inclusao
			 */
		    
		    
		}catch(IOException er){
			System.out.println(er.getMessage());
		}catch(Exception er){
			System.out.println(er.getMessage());
		}
		
	}

	private static void consultarPeca(){
		try{
			
			/*
			 * Chamando o metodo para consultar peca.
			 */
			
			Long idPeca = 729706L;
			HttpGet getRequest = new HttpGet(urlServicoPecas+idPeca);
			getRequest.addHeader("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04");
			
            HttpResponse response = new DefaultHttpClient().execute(getRequest);
			
            trataErro(response);			

			InputStream stream = response.getEntity().getContent();
			
			File file = new File("/home/87652404304/Desktop/Arquivo-Resposta");
			
			Files.write(file.toPath(), read(stream), StandardOpenOption.CREATE_NEW);
			
			/*
			 * Fim da chamada 
			 */
		    
		    
		}catch(IOException er){
			System.out.println(er.getMessage());
		}catch(Exception er){
			System.out.println(er.getMessage());
		}
		
	}
	
	private static void consultarIdPecas(){
		try{
			
			/*
			 * Chamando o metodo para consultar id das pecas
			 */
			
			String numProcesso = "300";
			HttpGet getRequest = new HttpGet(urlServicoPecas+"consulta-id-pecas/"+numProcesso);
			getRequest.addHeader("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04");
			
            HttpResponse response = new DefaultHttpClient().execute(getRequest);
			
            trataErro(response);			

			InputStream stream = response.getEntity().getContent();
			
			ObjectMapper mapper = new ObjectMapper();
			RespostaPecaIntegra resposta = mapper.readValue(stream, RespostaPecaIntegra.class);

			for(Long n: resposta.getListaResposta()){
				System.out.println(n);
			}
			
			/*
			 * Fim da chamada 
			 */
		    
		    
		}catch(IOException er){
			System.out.println(er.getMessage());
		}catch(Exception er){
			System.out.println(er.getMessage());
		}
		
	}
	
	private static void excluirDocumento(){
		try{
			
			/*
			 * Chamando o metodo para excluir documento
			 */
			
			Long idDoc = 729708l ;
			HttpDelete deleteRequest = new HttpDelete(urlServicoPecas+idDoc);
			deleteRequest.addHeader("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04");
			
            HttpResponse response = new DefaultHttpClient().execute(deleteRequest);
			
            trataErro(response);			

			InputStream stream = response.getEntity().getContent();
			
			Boolean retorno = new Boolean(new String(read(stream)));
			
			System.out.println(retorno);
			
			/*
			 * Fim da chamada 
			 */
		    
		    
		}catch(IOException er){
			System.out.println(er.getMessage());
		}catch(Exception er){
			System.out.println(er.getMessage());
		}
		
	}
	
	
	

	
	public static void main(String [] args){
		
		incluirPeca();
		consultarPeca();
		consultarIdPecas();
		excluirDocumento();
		
		
	}

}



class RespostaPecaIntegra implements Serializable {

	/**
	 * serial uid
	 */
	private static final long serialVersionUID = 624008075241190052L;

	private String observacao;
	
    private List<Long> listaResposta = new ArrayList<Long>();

    public RespostaPecaIntegra() {
    	
    }
    
    public RespostaPecaIntegra(List<Long> listaResposta) {
    	this.listaResposta = listaResposta;    	
    }
    

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<Long> getListaResposta() {
		return listaResposta;
	}

	public void setListaResposta(List<Long> listaResposta) {
		this.listaResposta = listaResposta;
	}

	

}


class ObjetoMergeRest {

	private String file;
	
	private String documento;

    protected List<String> listaCorrompidos = new ArrayList<String>();

    public ObjetoMergeRest() {
    	
    }

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public List<String> getListaCorrompidos() {
        return listaCorrompidos;
    }

    public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setListaCorrompidos(List<String> value) {
        this.listaCorrompidos = value;
    }
}




//public static void main(String args[]){
//
//
//try {
//	


	
//	File f = new File("/home/87652404304/Desktop/desktop/desk1/pdfs/arquivo_protegido.pdf");
//	
//	String boundary="asldkfjasdlfkalsdkfjlkj34lkjlsdkfj";
//	
//	HttpResponse<String> response = Unirest.post("http://localhost:8080/saj-ged/api/processo/documentos/recibo")
//			  .header("content-type", "multipart/form-data; boundary="+boundary)
//			  .header("Authorization", "Basic user:senha")
//			  .header("cache-control", "no-cache")
//			  .field("id_manifestacao",30L )
//			  .field("tipo_arquivo",30L )
//			  .field("dados", "".getBytes())
//			  .asString();			
////
////	HttpResponse<String> response = Unirest.post(urlServico+"recibo")
////			  .header("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
////			  .header("Authorization", "Basic "+userSenha)
////			  .header("cache-control", "no-cache")
////			  .field("id_manifestacao", 1000L )
////			  .field("tipo_arquivo", 7L) // sera sempre em pdf o recibo
////			  .field("dados", Files.newInputStream(f.toPath()))
////			  .asString();
////	
//	if (response.getStatus() != Response.Status.OK.getStatusCode()) {				
//		 throw new RuntimeException("Erro! "+response.getStatus()+" "+response.getBody().toString());
//	}
//	
//	System.out.println(new Integer(response.getBody()));
//	
//} catch (UnirestException e) {
//	e.printStackTrace();
//} catch(Exception er){
//	er.printStackTrace();
//}
//

//Client c = ClientBuilder.newClient();
//
//WebTarget baseTarget = c.target("http://localhost:8080/saj-ged/api/processo/documentos/201268");
//		
//Invocation.Builder invocationBuilder = baseTarget.request().header("Authorization", "Basic user:senha");
//		   
//Response response = invocationBuilder.get();
//
//if (response.getStatus() != Response.Status.OK.getStatusCode()) {
//	 throw new RuntimeException("Erro! "+response.getStatus());
//}
//File f = new File("./tempoFileClientRest");
//try {
//	
//	InputStream is = response.readEntity(InputStream.class);
//	
//	Files.copy(is, f.toPath(),StandardCopyOption.REPLACE_EXISTING );
//	
//	System.out.println(f.getAbsolutePath());
//	
//} catch (IOException e) {
//	e.printStackTrace();
//}


//
//
//Client c = ClientBuilder.newClient();
//
//WebTarget baseTarget = c.target("http://localhost:8080/saj-ged/api/processo/documentos/consulta-manifestacao-anexos/722");
//		
//Invocation.Builder invocationBuilder = baseTarget.request().header("Authorization", "Basic user:senha");
//		   
//Response response = invocationBuilder.get();
//
//if (response.getStatus() != Response.Status.OK.getStatusCode()) {
//	 throw new RuntimeException("Erro! "+response.getStatus());
//}
//File f = new File("./tempoFileClientRest72723");
//try {
//	
//	InputStream is = response.readEntity(InputStream.class);
//	
//	Files.copy(is, f.toPath(),StandardCopyOption.REPLACE_EXISTING );
//	
//	System.out.println(f.getAbsolutePath());
//	
//} catch (IOException e) {
//	e.printStackTrace();
//}
//
//
//
//


//Client c = ClientBuilder.newClient();
//
//WebTarget baseTarget = c.target("http://localhost:8080/saj-ged/api/processo/documentos/manifestacao-anexos/722");
//		
//Invocation.Builder invocationBuilder = baseTarget.request().header("Authorization", "Basic user:senha");
//		   
//Response response = invocationBuilder.get();
//
//if (response.getStatus() != Response.Status.OK.getStatusCode()) {
//	 throw new RuntimeException("Erro! "+response.getStatus());
//}
//File f = new File("./tempoFileClientRest");
//try {
//	
//	ObjetoMerge is = response.readEntity(ObjetoMerge.class);
//	
//	System.out.println(is.getClass());
//	System.out.println(is.getListaCorrompidos());
//	
//	Files.write(f.toPath(), is.getDocumento());
//	
//	System.out.println(f.getAbsolutePath());
//	
//} catch (IOException e) {
//	e.printStackTrace();
//}


//}
//
//
//}


//public static void main(String [] args){
//
//// Como produzir um metodo que gera um multipart/form-data
////
//// https://docs.jboss.org/resteasy/docs/1.1.GA/userguide/html/Multipart.html
//
//
//HttpClient client = new DefaultHttpClient();		
//HttpPost postRequest = new HttpPost ("http://localhost:8080/saj-ged/api/processo/documentos/recibo") ;
//postRequest.addHeader("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04");
//File f = new File("/home/87652404304/Desktop/desktop/desk1/pdfs/arquivo_protegido.pdf");
//
//try{
//	
//	System.out.println(((HttpURLConnection) new URL("http://localhost:8080/saj-ged/api/processo/documentos").openConnection()).getResponseCode());
//	
//	
//	
//    //Set various attributes
//    MultipartEntity part = new MultipartEntity();            
//    
//    parte.addPart("",null);
    
    
    //part.addPart("id_manifestacao", new StringBody("30"));
    //part.addPart("tipo_arquivo", new StringBody("7"));
//
//    InputStreamBody body = new InputStreamBody(Files.newInputStream(f.toPath()),f.getName());
////    FileBody fileBody = new FileBody(file, "application/octect-stream") ;
//    //Prepare payload
//    part.addPart("dados", body) ;
//
//    //Set to request body
//    postRequest.setEntity(part) ;
//     
//    //Send request
//    HttpResponse response = client.execute(postRequest) ;
//    
//    //Verify response if any
//    if (response != null){
//    	String a = new String(read(response.getEntity().getContent()));
//    	System.out.println(a);
//        System.out.println(response.getStatusLine().getStatusCode());
//    }
//}
//catch (Exception ex)     {
//    ex.printStackTrace() ;
//}
//
//
//}
//
//}


////FileBody fileBody = new FileBody(file, "application/octect-stream") ;
////Prepare payload
//part.addPart("dados", body) ;
//
////Set to request body
//postRequest.setEntity(part) ;
//
////Send request
//HttpResponse response = client.execute(postRequest) ;
//
////Verify response if any
//if (response != null){
//String a = new String(read(response.getEntity().getContent()));
//System.out.println(a);
//System.out.println(response.getStatusLine().getStatusCode());
//}


//Client c = ClientBuilder.newClient();
//
//WebTarget baseTarget = c.target("http://localhost:8080/saj-ged/api/processo/documentos/manifestacao-anexos/722");			
//Invocation.Builder invocationBuilder = baseTarget.request().header("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04");			   
//Response respon = invocationBuilder.get();
//
//try {
//	
////	System.out.println(respon.getEntity());
////	
////	respon.
//	
//	//ObjetoMerge is = mapper.convertValue(respon.getEntity(), ObjetoMerge.class);
//
//	ObjetoMerge is = respon.readEntity(ObjetoMerge.class);
//	
//	System.out.println(is.getClass());
//	System.out.println(is.getListaCorrompidos());
//	
//	Files.copy(new ByteArrayInputStream(is.getDocumento()), new File("/home/87652404304/Desktop/TTT-TT").toPath());
//	
//
//} catch (Exception e) {
//	e.printStackTrace();
//}



//ResteasyClient rClient = new ResteasyClientBuilder().build();	
//
//ResteasyWebTarget target = rClient.target("http://localhost:8080/saj-ged/api/processo/documentos/manifestacao-anexos-multipart/722");
//Response res = target.request().header("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04").get();
//
//System.out.println(res.getStatus());
//
//ObjetoMerge obj = target.proxy(ObjetoMerge.class);
//




//HttpGet getRequest = new HttpGet("http://localhost:8080/saj-ged/api/processo/documentos/consulta-manifestacao-anexos/722") ;
//getRequest.addHeader("Authorization", "Basic usr_ws_ged_des:@usr$ws#ged%des04");
//File f = new File("/home/87652404304/Desktop/desktop/desk1/pdfs/arquivo_protegido.pdf");

//try{
//
//	//Send request
//  HttpResponse response = client.execute(getRequest);
//  
//  //Verify response if any
//  if (response != null){
//  	File f = new File("/home/87652404304/Desktop/TEMP-PPPPPP");
//  	if(response.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
//  		Files.copy(response.getEntity().getContent(), f.toPath());
//  	}else{
//  		System.out.println(response.getStatusLine().getStatusCode());
//      	String a = new String(read(response.getEntity().getContent()));
//      	System.out.println(a);
//  	}
//  	
//      
//  }
//}
//catch (Exception ex)     {
//  ex.printStackTrace() ;
//}


	
//	public static void main(String args[]){
//		
//		Client c = ClientBuilder.newClient();
//		
//		WebTarget baseTarget = c.target("http://localhost:8080/saj-ged/api/");
//		baseTarget.path("/processo/documentos/201268");
//		
//		Invocation.Builder invocationBuilder = baseTarget.request();
//				   
//		Response response = invocationBuilder.get();
//		
//		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
//			 throw new RuntimeException("Erro listando contatos");
//		}
//		
//		System.out.println(response.getEntity());
//		BufferedOutputStream b = (BufferedOutputStream) response.getEntity();
//		
//		
//		
//	}

//}

