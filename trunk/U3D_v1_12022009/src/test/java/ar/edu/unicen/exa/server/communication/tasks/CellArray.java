package ar.edu.unicen.exa.server.communication.tasks;

import com.jme.math.Vector3f;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.IGridStructure;

public class CellArray implements IGridStructure {

		private static final long serialVersionUID=1L;
		private String worldID = "1";
		private Cell [] celdas;
		int pos=0;
		
		public CellArray(){
			celdas = new Cell[5];
		}
		
		public void addCelda(Cell celda)  {
		 celdas[pos]=celda;
		 pos++;
		}
		
		
		/*Por cuestiones de prueba todas las celdas son adyacentes*/
		@Override
		public Cell[] getAdjacents(Cell cell, Vector3f position) {
			
			Cell[] resultado=new Cell[4];
			//posicion en resultado
			int j=0;
			for(int i=0;i<5;i++)
				if(!celdas[i].equals(cell)){
					resultado[j]=celdas[i];
					j++;
				}
			return resultado;
		}

		@Override
		public Cell getCell(Vector3f position) {
			Cell celda=null;
			boolean encontrada=false;
			for(int i=0;i<5 && !encontrada;i++)
				if (celdas[i].isInside(position)){
					encontrada=true;
					celda=celdas[i];
				}
			return celda;
		}

		@Override
		public String getIdWorld() {
			return worldID;
		}

		@Override
		public Cell getSpawnCell() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Vector3f getSpawnPosition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setIdWorld(String id) {
			// TODO Auto-generated method stub
		}

		@Override
		public void setSpawnPosition(float x, float y, float z) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public long getId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setId(long anId) {
			// TODO Auto-generated method stub
			
		}

				
}


