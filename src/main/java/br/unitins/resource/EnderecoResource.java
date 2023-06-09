package br.unitins.resource;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import br.unitins.application.Result;
import br.unitins.dto.endereco.EnderecoDTO;
import br.unitins.dto.endereco.EnderecoResponseDTO;
import br.unitins.dto.usuario.UsuarioResponseDTO;
import br.unitins.service.endereco.EnderecoService;
import br.unitins.service.usuario.UsuarioService;

@Path("/enderecos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    @Inject
    EnderecoService enderecoservice;

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioService usuarioService;

    private static final Logger LOG = Logger.getLogger(EnderecoResource.class);

    @GET
    @RolesAllowed({ "Admin", "User" })
    public Response getUsuario() {

        // obtendo o login a partir do token
        String login = jwt.getSubject();
        UsuarioResponseDTO usuario = usuarioService.findByLogin(login);

        return Response.ok(usuario).build();
    }

    @GET
    @RolesAllowed({ "Admin" })
    public List<EnderecoResponseDTO> getAll() {
        LOG.info("Buscando todos os endereços.");
        LOG.debug("ERRO DE DEBUG.");
        return enderecoservice.getAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
    public EnderecoResponseDTO findbyId(@PathParam("id") Long id) {
        return enderecoservice.findById(id);
    }

    @POST
    @RolesAllowed({ "Admin" })
    public Response insert(EnderecoDTO enderecodto) {
        LOG.infof("Inserindo um endereço: %s", enderecodto.idCidade());
        Result result = null;
        try {
            EnderecoResponseDTO endereco = enderecoservice.create(enderecodto);
            LOG.infof("Endereço (%d) criado com sucesso.", endereco.id());
            return Response.status(Status.CREATED).entity(endereco).build();
        } catch (ConstraintViolationException e) {
            LOG.error("Erro ao incluir um endereço.");
            LOG.debug(e.getMessage());
            result = new Result(e.getConstraintViolations());
        } catch (Exception e) {
            LOG.fatal("Erro sem identificacao: " + e.getMessage());
            result = new Result(e.getMessage(), false);
        }
        return Response.status(Status.NOT_FOUND).entity(result).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
    public Response update(@PathParam("id") Long id, EnderecoDTO enderecodto) {
        LOG.infof("Atualizando um endereço: %s", enderecodto.idCidade());
        Result result = null;
        try {
            EnderecoResponseDTO endereco = enderecoservice.update(id, enderecodto);
            LOG.infof("Endereço (%d) atualizado com sucesso.", endereco.id());
            return Response.status(Status.NO_CONTENT).entity(endereco).build();
        } catch (ConstraintViolationException e) {
            LOG.error("Erro ao atualizar um endereço.");
            LOG.debug(e.getMessage());
            result = new Result(e.getConstraintViolations());
        } catch (Exception e) {
            LOG.fatal("Erro sem identificacao: " + e.getMessage());
            result = new Result(e.getMessage(), false);
        }
        return Response.status(Status.NOT_FOUND).entity(result).build();

    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ "Admin" })
    public Response delete(@PathParam("id") Long id) {
        enderecoservice.delete(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed({ "Admin" })
    public long count() {
        return enderecoservice.count();
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({ "Admin" })
    public List<EnderecoResponseDTO> search(@PathParam("nome") String nome) {
        return enderecoservice.findByNome(nome);

    }

}
