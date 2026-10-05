import { INavData } from '@coreui/angular';

export const navItems: INavData[] = [
  {
    name: 'Dashboard',
    url: '/dashboard',
    iconComponent: { name: 'cil-speedometer' },
    badge: {
      color: 'info',
      text: 'NEW'
    }
  },
  {
    name: 'Clients',
    url: '/clients',
    iconComponent: { name: 'cil-people' }
  },
  {
    name: 'Employes',
    url: '/employes',
    iconComponent: { name: 'cil-people' }
  },
  {
    name: 'Fournisseurs',
    url: '/fournisseur',
    iconComponent: { name: 'cil-people' }
  },

  {
    name: 'Produits',
    url: '/produit',
    iconComponent: { name: 'cil-people' }
  },
  {
    name: 'Facture',
    url: '/facture',
    iconComponent: { name: 'cil-people' }
  },
  {
    name: 'Ligne de facture',
    url: '/ligne_facture',
    iconComponent: { name: 'cil-people' }
  },
  {
    name: 'Achat',
    url: '/achat',
    iconComponent: { name: 'cil-people' }
  },

  {
    title: true,
    name: ''
  }


];
