package br.unitins.resource;

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
import br.unitins.dto.brinquedo.BrinquedoDTO;
import br.unitins.dto.brinquedo.BrinquedoResponseDTO;
import br.unitins.dto.usuario.UsuarioResponseDTO;
import br.unitins.service.brinquedo.BrinquedoService;
import br.unitins.service.usuario.UsuarioService;

import java.util.List;

@Path("/brinquedos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BrinquedoResource {

    @Inject
    BrinquedoService brinquedoService;

    @Inject
    JsonWebToken jwt;

    @Inject
    UsuarioService usuarioService;

    private static final Logger LOG = Logger.getLogger(BrinquedoResource.class);

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
    public List<BrinquedoResponseDTO> getAll() {
        LOG.info("Buscando todos os brinquedos.");
        LOG.debug("ERRO DE DEBUG.");
        return brinquedoService.getAll();

    }

    @GET
    @RolesAllowed({ "Admin" })
    @Path("/{id}")
    public BrinquedoResponseDTO findById(@PathParam("id") Long id) {
        return brinquedoService.findById(id);
    }

    @POST
    @RolesAllowed({ "Admin" })
    public Response insert(BrinquedoDTO brinquedodto) {
        LOG.infof("Inserindo um brinquedo: %s", brinquedodto.getClass());
        Result result = null;
        try {
            BrinquedoResponseDTO brinquedo = brinquedoService.create(brinquedodto);
            LOG.infof("Brinquedo (%d) criado com sucesso.", brinquedo.id());
            return Response.status(Status.CREATED).entity(brinquedo).build();
        } catch (ConstraintViolationException e) {
            LOG.error("Erro ao incluir um brinquedo.");
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
    public Response update(@PathParam("id") Long id, BrinquedoDTO brinquedodto) {
        LOG.infof("Atualizando um brinquedo: %s", brinquedodto.getClass());
        Result result = null;
        try {
            BrinquedoResponseDTO brinquedo = brinquedoService.update(id, brinquedodto);
            LOG.infof("Brinquedo (%d) atualizado com sucesso.", brinquedo.id());
            return Response.status(Status.NO_CONTENT).entity(brinquedo).build();
        } catch (ConstraintViolationException e) {
            LOG.error("Erro ao atualizar um brinquedo.");
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
        brinquedoService.delete(id);
        return Response
                .status(Status.NO_CONTENT)
                .build();
    }

    @GET
    @Path("/search/{id}")
    @RolesAllowed({ "Admin" })
    public BrinquedoResponseDTO searchId(@PathParam("id") Long id) {
        return brinquedoService.findById(id);
    }

    @GET
    @Path("/search/{nome}")
    @RolesAllowed({ "Admin" })
    public List<BrinquedoResponseDTO> search(@PathParam("nome") String nome) {
        return brinquedoService.findByNome(nome);
    }

    @GET
    @Path("/count")
    @RolesAllowed({ "Admin" })
    public long count() {
        return brinquedoService.count();
    }

}
