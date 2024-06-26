package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProveedoresBDD {
	 public ArrayList<Proveedor> buscar(String subcadena) throws KrakeDevException{
	    	ArrayList<Proveedor> proveedores=new ArrayList<Proveedor>();
	    	Connection con=null;
	    	PreparedStatement ps=null;
	    	ResultSet rs=null;
	    	Proveedor proveedor=null;
	    	try {
				con=ConexionBDD.obtenerConexion();
				ps=con.prepareStatement("select prov.identificador, prov.tipo_documento,td.descripcion,prov.nombre,prov.telefono,prov.correo,prov.direccion"
						+ " from proveedor prov, tipo_de_documento td "
						+ " where prov.tipo_documento = td.codigo_doc "
						+ " and upper (nombre) like  ? ");
				ps.setString(1, "%"+subcadena.toUpperCase()+"%");
				//System.out.println(">>>>>>>>>"+ps);
				rs=ps.executeQuery();
				
				while(rs.next()) {
					String identificador=rs.getString("identificador");
					String codigoTipoDocumento=rs.getString("tipo_documento");
					String descripcionTipoDoc=rs.getString("descripcion");
					String nombre=rs.getString("nombre");
					String telefono=rs.getString("telefono");
					String correo=rs.getString("correo");
					String direccion=rs.getString("direccion");
					TipoDocumento td=new TipoDocumento(codigoTipoDocumento,descripcionTipoDoc);
					proveedor = new Proveedor(identificador,td,nombre,telefono,correo,direccion);
					proveedores.add(proveedor);
				}
			} catch (KrakeDevException e) {
				e.printStackTrace();
				throw e;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KrakeDevException("Error al consultar. Detalle:"+e.getMessage());
			}
	    	return proveedores;
	    }
	 public void insertar(Proveedor proveedor) throws KrakeDevException{
		 Connection con=null;
		 PreparedStatement ps=null;
		 try {
	            con = ConexionBDD.obtenerConexion();
	            ps = con.prepareStatement("insert into proveedor(identificador, tipo_documento, nombre, telefono, correo, direccion) values(?,?,?,?,?,?)");
	            ps.setString(1, proveedor.getIdentificador());
	            ps.setString(2, proveedor.getTipoDocumento().getCodigo());
	            ps.setString(3, proveedor.getNombre());
	            ps.setString(4, proveedor.getTelefono());
	            ps.setString(5, proveedor.getCorreo());
	            ps.setString(6, proveedor.getDireccion());
	            
	            ps.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new KrakeDevException("Error al insertar el cliente. Detalle: "+e.getMessage());
	        } finally {
	            if (con != null) {
	                try {
	                    con.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	 }
	 
	 public Proveedor buscarIdentificadorP(String cadena) throws KrakeDevException{
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs= null;
			Proveedor proveedor = null;
			try {
				con = ConexionBDD.obtenerConexion();
				ps = con.prepareStatement(" select prov.identificador, prov.tipo_documento, td.descripcion, prov.nombre, prov.telefono, prov.correo, prov.direccion "
						+ " from proveedor prov, tipo_de_documento td "
						+ " where prov.tipo_documento = td.codigo_doc and identificador=? ");
				ps.setString(1, cadena);
				rs = ps.executeQuery();
				if (rs.next()) {
					String identificador = rs.getString("identificador");
					String codigoTipoDocumento = rs.getString("tipo_documento");
					String descripcionTd = rs.getString("descripcion");
					String nombre = rs.getString("nombre");
					String telefono = rs.getString("telefono");
					String correo = rs.getString("correo");
					String direccion = rs.getString("direccion");
					TipoDocumento tp= new TipoDocumento(codigoTipoDocumento,descripcionTd); 
					proveedor = new Proveedor(identificador, tp, nombre, telefono, correo, direccion);
				}
				
			} catch (KrakeDevException e) {
				e.printStackTrace();
				throw e;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new KrakeDevException("Error al consultar: "+e.getMessage());
			}
			return proveedor;
		}
}
