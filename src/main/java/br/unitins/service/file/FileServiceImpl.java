package br.unitins.service.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileServiceImpl implements FileService {

    private final String PATH_USER = System.getProperty("user.home")
        + File.separator + "quarkus"
        + File.separator + "images"
        + File.separator + "usuario" + File.separator;

    @Override
    public String salvarImagemUsuario(byte[] imagem, String nomeImagem) throws IOException {
        
       String mimeType = Files.probeContentType(new File(nomeImagem).toPath());
       List<String> listMimeType = Arrays.asList("image/jpg", "image/png");
       if (!listMimeType.contains(mimeType)){
            throw new IOException("Tipo de imagem não suportada.");
       }

       if(imagem.length > (1024 * 1024 * 10))
       throw new IOException("Arquivo muito grande.");

       File diretorio = new File(PATH_USER);
       if (!diretorio.exists())
            diretorio.mkdirs();
        
        String nomeArquivo = UUID.randomUUID()+"." +mimeType.substring(mimeType.lastIndexOf("/")+1);

        String path = PATH_USER + nomeArquivo;

        File file = new File(path);
        if(file.exists())
            throw new IOException("O nome gerado da imagem está repetido");

        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imagem);
        fos.flush();
        fos.close();

        return nomeArquivo;
    }

    @Override
    public File download(String nomeArquivo) {
        
        File file = new File(nomeArquivo);
        return file;
    }
    
}