package ibroker;

import java.rmi.RemoteException;

import beans.PackBean;
import beans.UserBean;

public class BuyService {
	
	public static int buy (PackBean pack, int shares, UserBean user, DBAccess rmi) {
		int existsPack = 0;	
		try {
			user.setSaldo(rmi.getInteger("SELECT bal AS return_value FROM users WHERE id = ?", user.getId()));
			existsPack = rmi.getInteger("SELECT COUNT(*) AS return_value FROM packs WHERE id_user = ? AND id_idea = ?", user.getUsername(), pack.getIdea());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int possible = user.getSaldo()/pack.getPrice();
		int buy = (possible >= shares) ? shares : possible;
		
		if (existsPack == 0) {
			//criar o pack novo
			try {
				rmi.setData("INSERT INTO packs (id_user, id_idea, num_av, num_un, price) VALUES (?, ?, ?, 0, ?)", user.getUsername(), pack.getIdea(), buy, pack.getPrice());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			//actualizar o pack
			try {
				rmi.setData("UPDATE packs SET num_av = num_av + ? WHERE id_user = ? AND id_idea = ?", buy, user.getUsername(), pack.getIdea());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(buy == pack.getShares()) {
			//remover o pack antigo
			try {
				rmi.setData("DELETE FROM packs WHERE id = ?", pack.getId());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			//actualizar o pack antigo (shares-buy)
			try {
				rmi.setData("UPDATE packs SET num_av = num_av - ? WHERE id_user = ? AND id_idea = ?", buy, pack.getOwner(), pack.getIdea());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			if(!user.getUsername().equals("root")){
				//descontar comprador buy
				rmi.setData("UPDATE users SET bal = bal - ? WHERE id = ?", buy*pack.getPrice(), user.getId());
			}
			//creditar vendedor buy
			rmi.setData("UPDATE users SET bal = bal + ? WHERE username = ?", buy*pack.getPrice(), pack.getOwner());
			//historico
			rmi.setData("INSERT INTO history (user1, user2, num_acc, id_idea, price, done, read) VALUES (?, ?, ?, ?, ?, 1, 0)", user.getUsername(), pack.getOwner(), buy, pack.getIdea(), pack.getPrice());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buy;
	}

}
