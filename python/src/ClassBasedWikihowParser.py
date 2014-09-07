#!/usr/bin/python

from bs4 import BeautifulSoup
from bs4.element import Tag 
from bs4.element import NavigableString
import os
import codecs
import lxml.etree as etree 
import lxml.builder

input_path = "../../data/html_sample"
output_path = "../../data/cleaned"

if not os.path.exists(output_path):
    os.makedirs(output_path)


def is_category_list(tag):
    return tag.name == 'ul' and tag.has_attr('id') and tag['id'] == 'breadcrumb'


def is_category_link(tag):
    return tag.name == 'a' and tag.has_attr('title') and tag['title'] != "Main Page" and not tag['title'].startswith(
        "Special")


def parse_category(tag):
    c_nodes = []
    for a in tag.find_all(is_category_link):
        c = etree.Element('c')
        c.text = a.get_text()
        c_nodes.append(c)
    return c_nodes


def parse_categories(soup):
    categories = etree.Element('categories')

    for cs in soup.find_all(is_category_list):
        category = etree.SubElement(categories,'category')
        for c in parse_category(cs):    
            category.append(c)
    
    return categories

    #return map(lambda category : parse_category(category), soup.find_all(is_category_list))


def is_step_listing(tag):
    return tag.name == 'ol' and tag.has_attr('class') and tag['class'][0].startswith('steps_list')


def is_step_tag(tag):
    return tag.name == 'b' and tag.has_attr('class') and tag['class'][0] == 'whb'


def is_clear_div(tag):
    return tag.name == 'div' and tag.has_attr('class') and tag['class'][0] == 'clearall'


def is_tips_div(tag):
    return tag.name == 'div' and tag.has_attr('id') and tag['id'] == 'tips'


def parse_ul_text(tag):
    ls = etree.Element('ul')
    for l in tag.find_all(lambda t : t.name == 'li'):
        li = etree.SubElement(ls,"li")
        li.text = l.get_text()

    return ls

def create_text_node(s):
    n = etree.Element('text')
    n.text = s
    return n

def parse_text(tag):
    if type(tag) is NavigableString:
        return create_text_node(unicode(tag.string))
    elif type(tag) is Tag:
        if tag.name == 'ul':
            return parse_ul_text(tag)
        elif tag.name == 'a':
            ref = tag['href'] if tag.has_attr('href') else ''
            title = tag['title'] if tag.has_attr('title') else ''
            anchor = etree.Element('a')
            anchor.set('href' ,ref)
            anchor.set('title' ,title)
            anchor.text = tag.get_text()
            return anchor
        else:
            return create_text_node(tag.get_text())

def get_text_from_tag_group(tags):
    s = []
    last_tag_name = None
    for tag in tags:
        current_tag_name = 'NavigableString' if type(tag) is NavigableString else tag.name
            
        if last_tag_name == None:
            s.append(parse_text(tag))
        elif current_tag_name == 'NavigableString' and last_tag_name == 'NavigableString':
            s.append(parse_text(tag))
        elif current_tag_name == 'a' and (last_tag_name == 'NavigableString' or last_tag_name == 'a'):
            s.append(parse_text(tag))
            
    return s


def parse_step_details(tag):
    tags_to_parse = []
    while not tag is None and not is_clear_div(tag):
        tags_to_parse.append(tag)
        tag = tag.next_sibling
    return get_text_from_tag_group(tags_to_parse)    
    


def parse_step_listing(tag):
    steps = []
    for step_em_tag in tag.find_all(is_step_tag):
        step = etree.Element('step')
        em = etree.SubElement(step,'em')
        em.text = step_em_tag.get_text()

        details = etree.SubElement(step,'details')
        for detail in parse_step_details(step_em_tag.next_sibling):
            details.append(detail)

        steps.append(step)
        
    return steps

    #return map(lambda b: [b.get_text(),parse_step_details(b.next_sibling)], tag.find_all(is_step_tag))


def parse_step_listings(soup):
    methods = etree.Element('methods')

    for listing in soup.find_all(is_step_listing):
        methods = etree.SubElement(methods,'method')
        for s in parse_step_listing(listing):
            methods.append(s)

    return methods
   # return map(lambda listing: parse_step_listing(listing),  soup.find_all(is_step_listing))


def parse_tip(tag):
    return map(lambda tip: parse_text(tip), tag.find_all(lambda t : t.name == 'li'))


def parse_tips(soup):
    return map(lambda tips : parse_tip(tips), soup.find_all(is_tips_div))


def parse_wiki_how(soup)
#a few todos:
#1. title
#2. other meta
#3. link to language
#4. relevant pages
    all_categories = parse_categories(soup)
    all_step_listings = parse_step_listings(soup)
    all_tips = parse_tips(soup)
    root = etree.Element('root')
    doc = etree.SubElement(root,'document')
    doc.append(all_categories)
    doc.append(all_step_listings)
    tipsNode = etree.SubElement(doc,'tips')
    for tips in all_tips:
        for tip in tips:
            tipNode = etree.SubElement(tipsNode,'tip')
            tipNode.append(tip)
    return root


def main():
    for wikihow_page_path in os.listdir(input_path):
        if not wikihow_page_path.endswith(".html"):
            continue
        wikihow_page = open(os.path.join(input_path, wikihow_page_path))
        soup = BeautifulSoup(wikihow_page)
        et = etree.ElementTree(root)
        et.write(os.path.join(output_path , wikihow_page_path + ".xml") ,xml_declaration=True, encoding='utf-8',pretty_print=True)

if __name__ == "__main__":
    main()
