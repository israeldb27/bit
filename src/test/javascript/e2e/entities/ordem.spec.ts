import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Ordem e2e test', () => {

    let navBarPage: NavBarPage;
    let ordemDialogPage: OrdemDialogPage;
    let ordemComponentsPage: OrdemComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Ordems', () => {
        navBarPage.goToEntity('ordem');
        ordemComponentsPage = new OrdemComponentsPage();
        expect(ordemComponentsPage.getTitle()).toMatch(/bcbApp.ordem.home.title/);

    });

    it('should load create Ordem dialog', () => {
        ordemComponentsPage.clickOnCreateButton();
        ordemDialogPage = new OrdemDialogPage();
        expect(ordemDialogPage.getModalTitle()).toMatch(/bcbApp.ordem.home.createOrEditLabel/);
        ordemDialogPage.close();
    });

    it('should create and save Ordems', () => {
        ordemComponentsPage.clickOnCreateButton();
        ordemDialogPage.tipoSelectLastOption();
        ordemDialogPage.setValorOrdemInput('5');
        expect(ordemDialogPage.getValorOrdemInput()).toMatch('5');
        ordemDialogPage.setQuantidadeInput('5');
        expect(ordemDialogPage.getQuantidadeInput()).toMatch('5');
        ordemDialogPage.setDataOrdemInput(12310020012301);
        expect(ordemDialogPage.getDataOrdemInput()).toMatch('2001-12-31T02:30');
        ordemDialogPage.statusSelectLastOption();
        ordemDialogPage.custodiaSelectLastOption();
        ordemDialogPage.save();
        expect(ordemDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class OrdemComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-ordem div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class OrdemDialogPage {
    modalTitle = element(by.css('h4#myOrdemLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    tipoSelect = element(by.css('select#field_tipo'));
    valorOrdemInput = element(by.css('input#field_valorOrdem'));
    quantidadeInput = element(by.css('input#field_quantidade'));
    dataOrdemInput = element(by.css('input#field_dataOrdem'));
    statusSelect = element(by.css('select#field_status'));
    custodiaSelect = element(by.css('select#field_custodia'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTipoSelect = function (tipo) {
        this.tipoSelect.sendKeys(tipo);
    }

    getTipoSelect = function () {
        return this.tipoSelect.element(by.css('option:checked')).getText();
    }

    tipoSelectLastOption = function () {
        this.tipoSelect.all(by.tagName('option')).last().click();
    }
    setValorOrdemInput = function (valorOrdem) {
        this.valorOrdemInput.sendKeys(valorOrdem);
    }

    getValorOrdemInput = function () {
        return this.valorOrdemInput.getAttribute('value');
    }

    setQuantidadeInput = function (quantidade) {
        this.quantidadeInput.sendKeys(quantidade);
    }

    getQuantidadeInput = function () {
        return this.quantidadeInput.getAttribute('value');
    }

    setDataOrdemInput = function (dataOrdem) {
        this.dataOrdemInput.sendKeys(dataOrdem);
    }

    getDataOrdemInput = function () {
        return this.dataOrdemInput.getAttribute('value');
    }

    setStatusSelect = function (status) {
        this.statusSelect.sendKeys(status);
    }

    getStatusSelect = function () {
        return this.statusSelect.element(by.css('option:checked')).getText();
    }

    statusSelectLastOption = function () {
        this.statusSelect.all(by.tagName('option')).last().click();
    }
    custodiaSelectLastOption = function () {
        this.custodiaSelect.all(by.tagName('option')).last().click();
    }

    custodiaSelectOption = function (option) {
        this.custodiaSelect.sendKeys(option);
    }

    getCustodiaSelect = function () {
        return this.custodiaSelect;
    }

    getCustodiaSelectedOption = function () {
        return this.custodiaSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
